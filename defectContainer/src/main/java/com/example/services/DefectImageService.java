package com.example.services;

import com.example.model.Defect;
import com.example.model.Location;
import com.example.repository.DefectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefectImageService {

    private static final String securityDefectsUrl = "http://security:8083/defects";
    @Autowired
    DefectRepository defectRepository;
    @Autowired
    RestTemplate restTemplate;

    @Transactional
    public byte[] getDefectImage(String authorizationHeader, Long defectId) throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", authorizationHeader);
        HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Object> validationResponse = restTemplate.exchange(securityDefectsUrl, HttpMethod.POST, requestEntity, Object.class);

        Defect defect = defectRepository.findById(defectId).orElseThrow(() -> new IllegalStateException("defect with id " + defectId + " does not exist"));
        //Optional<Defect> defect = defectRepository.findById(defectId).orElseThrow(() -> new IllegalStateException("defect with id " + defectId + " does not exist"));
        List<Location> locationList = defect.getLocationList();

        BufferedImage bufferedImage = ImageIO.read(defect.getDefectImageBlob().getBinaryStream());

        Blob defectImageBlob = defect.getDefectImageBlob();
        byte[] defectImageByte = defectImageBlob.getBytes(1, (int) defectImageBlob.length());
        int[] defectImageDimensions = getImageDimensionsFromBlob(defectImageBlob);

        Document document = createDocument(defectImageDimensions[0], defectImageDimensions[1], locationList);
        byte[] SVGImageByte = generateImageFromSVG(document);
        byte[] combinedImageByte = combineSVGImageWithDefectImage(SVGImageByte, defectImageByte, defectImageDimensions[0], defectImageDimensions[1]);

        defectImageBlob.free();

        return combinedImageByte;
    }

    private Document createDocument(int imageWidth, int imageHeight, List<Location> locationList) throws Exception {

        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

        Document document = impl.createDocument(svgNS, "svg", null);

        // Get the root element (the 'svg' element).
        Element root = document.getDocumentElement();
        root.setAttributeNS(null, "width", String.valueOf(imageWidth));
        root.setAttributeNS(null, "height", String.valueOf(imageHeight));

        for(Location location: locationList) {
            // Create and configure a circle based on the location of the defect
            Element circle = document.createElementNS(svgNS, "circle");
            circle.setAttributeNS(null, "cx", String.valueOf(location.getLocation().get(0)));
            circle.setAttributeNS(null, "cy", String.valueOf(location.getLocation().get(1)));
            circle.setAttributeNS(null, "r", "10");
            circle.setAttributeNS(null, "style", "fill:green");
            root.appendChild(circle);
        }

        return document;
    }

    // creates a png from an SVG
    private byte[] generateImageFromSVG(Document document) throws Exception {

        // create a JPEG transcoder
        PNGTranscoder pngTranscoder = new PNGTranscoder();
        //pngTranscoder.addTranscodingHint(JPEGTranscoder.KEY_FORCE_TRANSPARENT_WHITE, Color.WHITE);

        // Set the transcoder input
        TranscoderInput transcoderInput = new TranscoderInput(document);

        //  set the transcoder output
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TranscoderOutput transcoderOutput = new TranscoderOutput(byteArrayOutputStream);


        pngTranscoder.transcode(transcoderInput, transcoderOutput);
        byte[] renderedImageFromSVG = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        return renderedImageFromSVG;
    }

    // combines svg-image and defect-image
    private byte[] combineSVGImageWithDefectImage(byte[] SVGImageByte, byte[] defectImageByte, int imageWidth, int imageHeight) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(defectImageByte);
        BufferedImage bufferedDefectImage = ImageIO.read(inputStream);

        InputStream inputStream2 = new ByteArrayInputStream(SVGImageByte);
        BufferedImage bufferedSVGImage = ImageIO.read(inputStream2);

        BufferedImage bufferedCombinedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bufferedCombinedImage.createGraphics(); //a Graphics2D, used for drawing into this image.

        g.drawImage(bufferedDefectImage, 0, 0, null);
        g.drawImage(bufferedSVGImage, 0, 0, null);
        //graphics2D.dispose();

        ByteArrayOutputStream combinedImageByteOutputStream = new ByteArrayOutputStream();
        //OutputStream combinedImageByteOutputStream = new ByteArrayOutputStream();


        ImageIO.write(bufferedCombinedImage, "png", combinedImageByteOutputStream);


        byte[] combinedImageByte = combinedImageByteOutputStream.toByteArray();
        return combinedImageByte;

        //transcoder.addTranscodingHint(JPEGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
    }

    // returns the dimensions of an image
    public int[] getImageDimensionsFromBlob(Blob defectBlobImage) throws Exception {
        // Convert the BLOB to an InputStream
        InputStream inputStream = defectBlobImage.getBinaryStream();

        // Read the image from the InputStream
        BufferedImage image = ImageIO.read(inputStream);

        // Get the image width and height
        int width = image.getWidth();
        int height = image.getHeight();

        return new int[] {width, height};
    }
}
