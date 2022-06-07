# Glitchio - Interactive image processing for Android


## What is this app for?

GlitchIO is an app for image manipulation for android devices which allows users to transform
images in various abstract ways. The app offers a selection of image processing effects which all
offer different ways of manipulating an image. Users can add a series of effects to transform
images or add subtle effects to make slight changes to an image.


## Effects

The effects designed for the app fall into three general categories – effects that create changes in colour, 
effects that displace pixels and advanced effects that involve iteration. Examples what transformation can be seen in the
sections below. To gain knowledge on the topic I used a variety of resources available online.
Some of the resources that I found most helpful are The Book of Shaders (Vivo, P. and Lowe, J.,
2022) and the tutorials available on ciphrd.com. Additionally I made research on topics like
random numbers, noise.
Some of the methods require utility functions that are not available in OpenGL. This includes a
way to generate random numbers and a function that converts between RGB and HVS values.
The solution is to create functions for this in the OpenGL shaders. A way of generating random
numbers is solution to this is provided in the The Book of Shaders. Another solution to this is
available on stackoverflow.com which was used for the purposes of this project. (Stack
Overflow | Random / noise functions for GLSL, 2022)
Colour effects make changes to the colour space in the image. This can be done by converting
the RGB values into hue, saturation and value parameters. Transforming these values can
create effects like shifting the hue of the image or focusing on a certain set of colours. The
formula for converting HSV is available at stackoverflow.com (Stack Overflow | From RGB to
HSV in OpenGL GLSL, 2022). I have included a set of examples for each effect paired with
pseudo code used to produce them. The variables labeled paramFloat1, paramFloat2, etc.
correspond to parameters controlled by the user while the variables hue and hueMapped
correspond to pixel values taken from the input image.
