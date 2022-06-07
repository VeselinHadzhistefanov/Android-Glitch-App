# Glitchio - Interactive image processing for Android


## What is this app for?

GlitchIO is an app for image manipulation for android devices which allows users to transform
images in various abstract ways. The app offers a selection of image processing effects which all
offer different ways of manipulating an image. Users can add a series of effects to transform
images or add subtle effects to make slight changes to an image.


## Effects

The effects designed for the app fall into three general categories: 
- effects that create changes in colour, 
- effects that displace pixels 
- advanced effects that involve iteration. 

## Colour
- Colour effects make changes to the colour space in the image. This can be done by converting
the RGB values into hue, saturation and value parameters. Transforming these values can
create effects like shifting the hue of the image or focusing on a certain set of colours. 
The variables labeled paramFloat1, paramFloat2, etc. correspond to parameters controlled by the user 
while the variables hue and hueMapped correspond to pixel values taken from the input image.


### Hue Shift

```c++
float hueMapped = mod(hue + paramFloat1, 1.0); //rotate and map hue to 0,1 range
```
<img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/1%20Hue%20Shift.jpg" />


### Hue Focus
```c++
hue = hue * 2.0 – 1.0; // map hue to 1,-1 range
float hueMapped = sign(hue)*pow(hue, paramFloat1); // apply an exponent to the hue
hueMapped = hueMapped / 2.0 + 0.5; // convert hue to 0,1 range
```
<img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/2.1%20Hue%20Focus.jpg" />

