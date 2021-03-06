/**
 * Copyright 2017, Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vision;


import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.ColorInfo;
import com.google.cloud.vision.v1.DominantColorsAnnotation;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.LocationInfo;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Detect {

  /**
   * Detects entities,sentiment and syntax in a document using the Natural Language API.
   * @throws IOException on Input/Output errors.
   */
  public static void main(String[] args) throws IOException {
    argsHelper(args, System.out);
  }

  /**
   * Helper that handles the input passed to the program.
   * @throws IOException on Input/Output errors.
   */
  public static void argsHelper(String[] args, PrintStream out) throws IOException {
    if (args.length < 1) {
      out.println("Usage:");
      out.printf(
          "\tjava %s \"<command>\" \"<path-to-image>\"\n"
          + "Commands:\n"
          + "\tall-local | faces | labels | landmarks | logos | text | safe-search | properties\n"
          + "Path:\n\tA file path (ex: ./resources/wakeupcat.jpg) or a URI for a Cloud Storage "
          + "resource (gs://...)\n",
          Detect.class.getCanonicalName());
      return;
    }
    String command = args[0];
    String path = args.length > 1 ? args[1] : "";

    Detect app = new Detect(ImageAnnotatorClient.create());
    if (command.equals("all-local")) {
      detectFaces("resources/face_no_surprise.jpg", out);
      detectLabels("resources/wakeupcat.jpg", out);
      detectLandmarks("resources/landmark.jpg", out);
      detectLogos("resources/logos.png", out);
      detectText("resources/text.jpg", out);
      detectProperties("resources/landmark.jpg", out);
      detectSafeSearch("resources/wakeupcat.jpg", out);
    } else if (command.equals("faces")) {
      if (path.startsWith("gs://")) {
        // TODO: See https://goo.gl/uWgYhQ
      } else {
        detectFaces(path, out);
      }
    } else if (command.equals("labels")) {
      if (path.startsWith("gs://")) {
        // TODO: See https://goo.gl/uWgYhQ
      } else {
        detectLabels(path, out);
      }
    } else if (command.equals("landmarks")) {
      if (path.startsWith("gs://")) {
        // TODO: See https://goo.gl/uWgYhQ
      } else {
        detectLandmarks(path, out);
      }
    } else if (command.equals("logos")) {
      if (path.startsWith("gs://")) {
        // TODO: See https://goo.gl/uWgYhQ
      } else {
        detectLogos(path, out);
      }
    } else if (command.equals("text")) {
      if (path.startsWith("gs://")) {
        // TODO: See https://goo.gl/uWgYhQ
      } else {
        detectText(path, out);
      }
    } else if (command.equals("properties")) {
      if (path.startsWith("gs://")) {
        // TODO: See https://goo.gl/uWgYhQ
      } else {
        detectProperties(path, out);
      }
    } else if (command.equals("safe-search")) {
      if (path.startsWith("gs://")) {
        // TODO: See https://goo.gl/uWgYhQ
      } else {
        detectSafeSearch(path, out);
      }
    }
  }

  private static ImageAnnotatorClient visionApi;

  /**
   * Constructs a {@link Detect} which connects to the Cloud Vision API.
   */
  public Detect(ImageAnnotatorClient client) {
    visionApi = client;
  }

  /**
   * Detects faces in the specified image.
   * @param filePath The path to the file to perform face detection on.
   * @param out A {@link PrintStream} to write detected features to.
   * @throws IOException on Input/Output errors.
   */
  public static void detectFaces(String filePath, PrintStream out) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();

    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build();
    requests.add(request);

    BatchAnnotateImagesResponse response = visionApi.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        out.printf("Error: %s\n", res.getError().getMessage());
        return;
      }

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
        out.printf("anger: %s\njoy: %s\nsurprise: %s\nposition: %s",
            annotation.getAngerLikelihood(),
            annotation.getJoyLikelihood(),
            annotation.getSurpriseLikelihood(),
            annotation.getBoundingPoly());
      }
    }
  }

  /**
   * Detects labels in the specified image.
   * @param filePath The path to the file to perform label detection on.
   * @param out A {@link PrintStream} to write detected labels to.
   * @throws IOException on Input/Output errors.
   */
  public static void detectLabels(String filePath, PrintStream out) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();

    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build();
    requests.add(request);

    BatchAnnotateImagesResponse response = visionApi.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        out.printf("Error: %s\n", res.getError().getMessage());
        return;
      }

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
        annotation.getAllFields().forEach((k, v)->out.printf("%s : %s\n", k, v.toString()));
      }
    }
  }

  /**
   * Detects landmarks in the specified image.
   * @param filePath The path to the file to perform landmark detection on.
   * @param out A {@link PrintStream} to write detected landmarks to.
   * @throws IOException on Input/Output errors.
   */
  public static void detectLandmarks(String filePath, PrintStream out) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();
    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build();
    requests.add(request);

    BatchAnnotateImagesResponse response = visionApi.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        out.printf("Error: %s\n", res.getError().getMessage());
        return;
      }

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
        LocationInfo info = annotation.getLocationsList().listIterator().next();
        out.printf("Landmark: %s\n %s\n", annotation.getDescription(), info.getLatLng());
      }
    }
  }

  /**
   * Detects logos in the specified image.
   * @param filePath The path to the file to perform logo detection on.
   * @param out A {@link PrintStream} to write detected logos to.
   * @throws IOException on Input/Output errors.
   */
  public static void detectLogos(String filePath, PrintStream out) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();

    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().setType(Type.LOGO_DETECTION).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build();
    requests.add(request);

    BatchAnnotateImagesResponse response = visionApi.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        out.printf("Error: %s\n", res.getError().getMessage());
        return;
      }

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
        out.println(annotation.getDescription());
      }
    }
  }

  /**
   * Detects text in the specified image.
   * @param filePath The path to the file to detect text in.
   * @param out A {@link PrintStream} to write the detected text to.
   * @throws IOException on Input/Output errors.
   */
  public static void detectText(String filePath, PrintStream out) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();

    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build();
    requests.add(request);

    BatchAnnotateImagesResponse response = visionApi.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        out.printf("Error: %s\n", res.getError().getMessage());
        return;
      }

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
        out.printf("Text: %s\n", annotation.getDescription());
        out.printf("Position : %s\n", annotation.getBoundingPoly());
      }
    }
  }

  /**
   * Detects image properties such as color frequency from the specified image.
   * @param filePath The path to the file to detect properties.
   * @param out A {@link PrintStream} to write
   * @throws IOException on Input/Output errors.
   */
  public static void detectProperties(String filePath, PrintStream out) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();

    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().setType(Type.IMAGE_PROPERTIES).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build();
    requests.add(request);

    BatchAnnotateImagesResponse response = visionApi.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        out.printf("Error: %s\n", res.getError().getMessage());
        return;
      }

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
      for (ColorInfo color : colors.getColorsList()) {
        out.printf("fraction: %f\nr: %f, g: %f, b: %f\n",
            color.getPixelFraction(),
            color.getColor().getRed(),
            color.getColor().getGreen(),
            color.getColor().getBlue());
      }
    }
  }

  /**
   * Detects whether the specified image has features you would want to moderate.
   * @param filePath The path to the file used for safe search detection.
   * @param out A {@link PrintStream} to write the results to.
   * @throws IOException on Input/Output errors.
   */
  public static void detectSafeSearch(String filePath, PrintStream out) throws IOException {
    List<AnnotateImageRequest> requests = new ArrayList<>();

    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

    Image img = Image.newBuilder().setContent(imgBytes).build();
    Feature feat = Feature.newBuilder().setType(Type.SAFE_SEARCH_DETECTION).build();
    AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat)
        .setImage(img)
        .build();
    requests.add(request);

    BatchAnnotateImagesResponse response = visionApi.batchAnnotateImages(requests);
    List<AnnotateImageResponse> responses = response.getResponsesList();

    for (AnnotateImageResponse res : responses) {
      if (res.hasError()) {
        out.printf("Error: %s\n", res.getError().getMessage());
        return;
      }

      // For full list of available annotations, see http://g.co/cloud/vision/docs
      SafeSearchAnnotation annotation = res.getSafeSearchAnnotation();
      out.printf("adult: %s\nmedical: %s\nspoofed: %s\nviolence: %s\n",
          annotation.getAdult(),
          annotation.getMedical(),
          annotation.getSpoof(),
          annotation.getViolence());
    }
  }
}
