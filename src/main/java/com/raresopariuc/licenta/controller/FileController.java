package com.raresopariuc.licenta.controller;

import com.raresopariuc.licenta.model.Apartment;
import com.raresopariuc.licenta.model.Car;
import com.raresopariuc.licenta.model.DBFile;
import com.raresopariuc.licenta.model.House;
import com.raresopariuc.licenta.payload.ApiResponse;
import com.raresopariuc.licenta.payload.UploadFileResponse;
import com.raresopariuc.licenta.repository.ApartmentRepository;
import com.raresopariuc.licenta.repository.CarRepository;
import com.raresopariuc.licenta.repository.DBFileRepository;
import com.raresopariuc.licenta.repository.HouseRepository;
import com.raresopariuc.licenta.service.DBFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private DBFileStorageService DBFileStorageService;

    @Autowired
    private DBFileRepository dbFileRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private CarRepository carRepository;

    @PostMapping("/uploadFilepond")
    public UploadFileResponse uploadFilepond(@RequestParam("filepond") MultipartFile file) {
        DBFile dbFile = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/files/downloadFile/")
                .path(dbFile.getId())
                .toUriString();

        return new UploadFileResponse(dbFile.getId(), dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @DeleteMapping("/uploadFilepond")
    public ResponseEntity<?> deleteFilepond(@RequestBody String input) {

//        Gson gson = new Gson();
//        JsonObject jsonObject = gson.fromJson(input, JsonObject.class);
//        String fileId = jsonObject.get("id").getAsString();

        //removeFileFromHouse(DBFileStorageService.getFile(input));

        DBFile pictureFile = DBFileStorageService.getFile(input);
        Optional<House> house = houseRepository.findHouseByPictureFiles_IdEquals(pictureFile.getId());
        Optional<Apartment> apartment = apartmentRepository.findApartmentByPictureFiles_IdEquals(pictureFile.getId());
        Optional<Car> car = carRepository.findCarByPictureFiles_IdEquals(pictureFile.getId());

        if (house.isPresent()) {
            house.get().getPictureFiles().remove(pictureFile);
            houseRepository.save(house.get()); //pictureFile remains orphan and is automatically deleted
        } else if (apartment.isPresent()) {
            apartment.get().getPictureFiles().remove(pictureFile);
            apartmentRepository.save(apartment.get());
        } else if (car.isPresent()) {
            car.get().getPictureFiles().remove(pictureFile);
            carRepository.save(car.get());
        } else {
            dbFileRepository.delete(DBFileStorageService.getFile(input));
        }

        return dbFileRepository.findById(input).isPresent() ?
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(false, "File wasn't deleted!", input)) :
                ResponseEntity.ok()
                        .body(new ApiResponse(true, "File successfully deleted!", input));
    }

    @GetMapping("/uploadFilepond")
    public ResponseEntity<?> getFilepond(@RequestParam("load") String input) {
        DBFile dbFile = DBFileStorageService.getFile(input);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        DBFile dbFile = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/files/downloadFile/")
                .path(dbFile.getId())
                .toUriString();

        return new UploadFileResponse(dbFile.getId(), dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        DBFile dbFile = DBFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    private void removeFileFromHouse(DBFile pictureFile) {
        Optional<House> house = houseRepository.findHouseByPictureFiles_IdEquals(pictureFile.getId());
        house.ifPresent(houseContainingPicture -> {
            houseContainingPicture.getPictureFiles().remove(pictureFile);
            houseRepository.save(houseContainingPicture);
        });
    }
}