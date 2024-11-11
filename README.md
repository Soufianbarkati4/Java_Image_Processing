# Java_Image_Processing
Java Image Processing Application

*Project Overview :*

This project is a Java-based application for image processing. It provides functionalities to load, manipulate, and transform images. The project includes classes for handling colored and grayscale images, applying various treatments, and displaying images in a user interface. The application is structured to handle potential errors during image processing using custom exceptions.

*Project Structure :*

Core Classes :

  - Image.java: Manages the core properties and operations of an image object, including loading and storing pixel data.
  - TraitementImage.java: A base class for image processing, providing foundational methods for manipulating images.
  - TraitementImageColored.java: Extends TraitementImage to handle transformations specific to colored images.
  - TraitementImageGrey.java: Extends TraitementImage to apply processing techniques specific to grayscale images.
  - Interface and Error Handling
  - InterfaceUtilisateur.java: Provides a user interface for loading and displaying images, allowing users to apply transformations interactively.
  - ImageFailException.java: Custom exception to handle image loading or processing errors, ensuring robust error handling in the application.
