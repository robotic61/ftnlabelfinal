package com.forthems.ftnlabel.controller;


import com.forthems.ftnlabel.model.Ftn;
import com.forthems.ftnlabel.service.FtnService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FtnController {

    private final FtnService ftnService;

    public FtnController(FtnService ftnService) {
        this.ftnService = ftnService;
    }


    @GetMapping("/findbyftn")
    public Ftn findByFtnNo(@RequestParam String word) {
        return ftnService.findByFtnNo(word);
    }

    @GetMapping("/ftnlabel")
    public ResponseEntity<byte[]> ftnLabelCreation(@RequestParam String ftnNo) throws Exception {
        byte[] pdfBytes = ftnService.ftnLabelCreation(ftnNo);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
    }

}

//    @GetMapping("/ftn/product")
//    public ResponseEntity<byte[]> createLabel(@RequestParam String ftnNo) throws Exception {
//        byte[] pdfBytes = ftnService.ftnLabelCreationtester();
//
//        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
//    }

//    @GetMapping("/ftn/productlabel")
//    public ResponseEntity<byte[]> createLabel() throws Exception {
//        byte[] pdfBytes = ftnService.ftnLabelCreationtester();
//
//        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
//    }


    /*

    Create PDF in RAM (memory)
            ↓
    Convert to byte[]
            ↓
    Spring sends bytes to client through HTTP response
            ↓
    Method finishes
            ↓
    Memory becomes unused
            ↓
    Java Garbage Collector eventually removes it

    ResponseEntity<byte[]>: the response body contains byte[]

    HTTP response has 3 major parts:

    Status code
    Headers
    Body

    ResponseEntity lets us control all 3.

    1. Status
    ok()

    sends:

    HTTP 200 OK

    2. Headers
    .contentType(MediaType.APPLICATION_PDF)

    sends:

    Content-Type: application/pdf

    telling browser/client:

    "This response is a PDF"

    3. Body
    .body(pdfBytes)

    sends the actual PDF bytes.

    ResponseEntity = full HTTP response builder.

     */

    /*
    @GetMapping("/pdf")
    public byte[] pdf() {
        return pdfBytes;
    }

    Spring can also send byte[] directly.

    but:

    content type may be unclear
    less control

    Without:

    .contentType(MediaType.APPLICATION_PDF)

    Spring may not know the response is a PDF.

    So browser/client may:

    download strangely
    show raw bytes
    use the wrong content type
     */

    //return PDF file, filename = ftn_label.pdf

    // try without writing files (create files in our own system).
    // write files is slow and need to delete the file after.
    // what if the multiple users use, then the file will overwrite each other.

    // change content to a byte array?
