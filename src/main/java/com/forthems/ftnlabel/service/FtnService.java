package com.forthems.ftnlabel.service;


import com.forthems.ftnlabel.model.Ftn;
import com.forthems.ftnlabel.repository.FtnRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FtnService {

    private final FtnRepository ftnRepository;

    public FtnService(FtnRepository ftnRepository) {
        this.ftnRepository = ftnRepository;
    }

    public BufferedImage generateQRCode(String text, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);

        return MatrixToImageWriter.toBufferedImage(matrix);
    }


    public Ftn findByFtnNo(String word) {
        return ftnRepository.findByFtnNo(word);
    }

    /*
    Sizing

    1 inch = 72 units
    1 inch = 2.54 cm

    1 cm = 1/2.54 inch
    8.5 cm = 1/2.54 x 8.5 = 3.346 inch
    4.5 cm = 1/2.54 x 4.5 = 1.772 inch

    Thus
    8.5 cm = 3.346 x 72 = 240.912f units
    4.5 cm = 1.772 x 72 = 127.584f units
    */

    // create a method that gets the data retrieved from the database
    // from findByFtnNo method and uses with the ftnLabelCreation() and
    // generateQRCode method.

    public String safe(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return "" + obj;
        }
    }
    // to handle the case where the field is null(empty)
    // so it doesnt show "null" on the pdf

    public byte[] ftnLabelCreation(String ftnNo) throws Exception {
        // pdf file = binary data (bytes)
        /*

       PDFBox creates PDF
                ↓
       saved into ByteArrayOutputStream
                ↓
       converted into byte[]
                ↓
       returned to controller
                ↓
       sent to client

         */

        // to get the data from the database as an object.
        Ftn ftn = findByFtnNo(ftnNo);
        try (PDDocument document = new PDDocument()) {
            PDRectangle customSize = new PDRectangle(240.912f, 127.584f);
            PDPage page = new PDPage(customSize);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Left side of the paper
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(55, 110);
                contentStream.showText("P/N:");
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 7);
                contentStream.newLineAtOffset(15, 0);
                // Placeholder for P/N : Material_No
                contentStream.showText(safe(ftn.getPn()));
                contentStream.endText();
                // endText to avoid coordinates of each row being dependent on each other.

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(20, 80); // 20 units away from the left side of the paper
                contentStream.showText("Desc:");
                contentStream.newLineAtOffset(20, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 5);
                contentStream.showText(safe(ftn.getDescription()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(20, 65); //
                contentStream.showText("Brand:");
                contentStream.newLineAtOffset(20, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.showText(safe(ftn.getBrand()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(20, 55); // -10 units from the lower label
                contentStream.showText("Maker:");
                contentStream.newLineAtOffset(20, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.showText(safe(ftn.getMaker()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(20, 45); // -10 units from the lower label
                contentStream.showText("D/C:");
                contentStream.newLineAtOffset(20, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.showText(safe(ftn.getDc()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(20, 35); // -10 units from the lower label
                contentStream.showText("L/C:");
                contentStream.newLineAtOffset(20, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.showText(safe(ftn.getLotCode()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(20, 25); // -10 units from the lower label
                contentStream.showText("Cust.BAT:");
                contentStream.newLineAtOffset(32, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.showText(safe(ftn.getCustBatch()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(20, 15); // -10 units from the lower label
                contentStream.showText("Cust.P/N:");
                contentStream.newLineAtOffset(32, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.showText(safe(ftn.getCustPart()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(180, 110);
                contentStream.showText("MSL:");
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 7);
                contentStream.newLineAtOffset(20, 0);
                // Placeholder for P/N : Material_No
                contentStream.showText(safe(ftn.getMsl()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(70, 45);
                contentStream.showText("Exp:");
                contentStream.newLineAtOffset(15, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
                if (ftn.getExp() == null) {
                    contentStream.showText("");
                } else {
                    contentStream.showText(ftn.getExp().format(formatter));
                }
                // Can I remove 00:00:00.000?
                // do I need to show time? Not showing time for now
                // since the expiry date normally doesn't show the time, and in the given PDF it doesn't show time.
                // QR code returns the value of the String passed into the method, right?
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 7);
                contentStream.newLineAtOffset(80, 0);
                contentStream.showText("FTN"); // 2001000026
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 7);
                contentStream.newLineAtOffset(18, 0);
                contentStream.showText(safe(ftn.getFtnNo()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(165, 35);
                contentStream.showText("Batch:");
                contentStream.newLineAtOffset(21, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.showText(safe(ftn.getBatch()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 6);
                contentStream.newLineAtOffset(150, 25);
                contentStream.showText("Shelf:");
                contentStream.newLineAtOffset(18, 0);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.showText(safe(ftn.getShelf()));
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 5);
                contentStream.newLineAtOffset(27, 1.5f);
                contentStream.showText(safe(ftn.getProject()));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 7);
                contentStream.newLineAtOffset(167, 15);
                contentStream.showText("Qty:");
                contentStream.newLineAtOffset(17, 0);
                contentStream.showText(safe(ftn.getQty()));
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
                contentStream.newLineAtOffset(28, 2.5f);
                contentStream.showText(safe(ftn.getBaseUnit()));
                contentStream.endText();


                // P/N(Material_No) Qr code
                BufferedImage qrCodePn = generateQRCode(safe(ftn.getPn()), 164, 164);
                // QR image pixels should be about 4 times bigger than IMAGE size
                // so its clear when printing.
                // here we create an image that is 164 pixels wide x 164 pixels tall
                // pixel = tiny dots that make up an image.
                // more pixels = more detail


                PDImageXObject qrCodeImagePn = LosslessFactory.createFromImage(document, qrCodePn);
                // Changes java image into a PDFBox image, since PDFBox cannot directly draw
                // Buffered Image.
                // “Lossless” = no quality loss during conversion
                // We need a document because the image must be created and stored inside a specific PDF document before it can be drawn.

                contentStream.drawImage(qrCodeImagePn, 13.5f, 84.5f, 41, 41);
                // we draw the qrcode image at (13.5f, 84.5f) is
                // the bottom-left corner of the image
                // we create an image of width = 41 and height = 41
                // PDFBox scales the image to fit 41 × 41 points

                // Ftn_no qr code
                BufferedImage qrCodeFtn = generateQRCode(safe(ftn.getFtnNo()), 164, 164);
                // QR image pixels should be about 4 times bigger than IMAGE size
                // so its clear when printing.
                // here we create an image that is 164 pixels wide x 164 pixels tall
                // pixel = tiny dots that make up an image.
                // more pixels = more detail

                PDImageXObject qrCodeImageFtn = LosslessFactory.createFromImage(document, qrCodeFtn);
                // Changes java image into a PDFBox image, since PDFBox cannot directly draw
                // Buffered Image.
                // “Lossless” = no quality loss during conversion
                // We need a document because the image must be created and stored inside a specific PDF document before it can be drawn.

                // contentStream.newLineAtOffset(183, 55);
                contentStream.drawImage(qrCodeImageFtn, 176, 50.5f, 41, 41);

            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            document.save(outputStream);

            return outputStream.toByteArray();

        }
    }
}

    /*
    method to extract labels
    method to put labels
    method to create template for the product label
   (like:
   Desc:
   Brand:
   Maker:
   )

   What to do if the field is null? shows empty string(nothing)
   Where to get the text to create the Qr code?
   from
   1. "ftnNo": "2001000027"
   2. "pn": "10101-0040-02962"
   for 2 qr codes

   Make template for the paper like where to put the texts for each field
   then just input the ftnno to the method and then info will be input into the
   pdf

   use the object after acquiring from the database, to paste into the label.

   use any font to be nicely readable.
     */

    /*
    Next:
    1. Can I remove 00:00:00.000 (maybe include it but match exactly 00:00:00.000).
    2. How to remove null and place empty string instead(nothing).
     */