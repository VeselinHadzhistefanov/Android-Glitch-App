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
<img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/2%20Hue%20Focus.jpg" />


### Hue Distort
```c++
float hueMapped = mod(hue * paramFloat1 * 10.0, 1.0); // multiply hue by a value
```
<img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/3%20Hue%20Distort.jpg" />

### Value Shift
The same transformations can be applied to the value parameter of each pixel.
```c++
float valueMapped = mod(value + paramFloat1, 1.0); //rotate and map value to 0,1 range
```
<img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/4%20Value%20Shift.jpg" />

## Distortion Effects
- The second set of effects is labeled Distort. The idea behind these effects is to create distorted
images by transforming pixel locations according to some rules. One of the effects I explored
generates a gradient, blending between two parts of the image. This effect is called interpolate
as it interpolates between pixel values. This effect is controlled by two parameters which
determine positioning of the gradient effect.

### Interpolate

```c++
float y1 = paramFloat1; // vertical position 1
float y2 = paramFloat2; // vertical position 2
if(currentY > y1 && currentY < y2){ // draw in a rectangle
 vec4 colour1 = texture2D(u_Texture, vec2(currentX, y1)); // Obtain colour 1
 vec4 colour2 = texture2D(u_Texture, vec2(currentX, y2)); // Obtain colour 2
 float interpolate = (currentY - y1) / (y2 - y1); // Get interpolation position

 pixelColour = colour1 * (1.0-interpolate) + colour2 * interpolate
 ```
 <img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/6%20Interpolate.jpg" />```
 
 
 ### Expand – This effect is achieved by copying pixel values vertically.
 
 ```c++
float y1 = paramFloat1; // vertical position
float size = paramFloat2; // height
if(currentY > y1 && currentY < y1 + size){ // draw in a rectangle
 colour = texture2D(u_Texture, vec2(currentX, y1)); // replace pixels
}
else{
 colour = texture2D(u_Texture, vec2(currentX, currentY));
 }
```
 <img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/7%20Expland.jpg" />```



### Wave - An effect that makes use of a wave function to change the position of pixels.

```c++
float amp = paramFloat1; // Amount of wave displacement
float freq = paramFloat2; // Length of the wave pattern
float xPos = currentX;
float yPos = currentY + cos(currentX * freq * PI * 2.0)*amp; // Modify the Y position
vec3 colour = texture2D(u_Texture, vec2(xPos, yPos)); // Take new value from texture
```
 <img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/8%20Wave.jpg" />```


### Random distort - The same can be done with a noise which disperses pixel randomly. This
creates a fluid-like structure. The noise function used takes 2 parameters – the coordinates of
the image and returns an interpolated random value for these coordinates. The size of the
smoothing can be controlled as well as the amount of displacement. This method for generation
noise is described in detail in The Book of Shaders.

```c++
float amt = paramFloat1; // Set the amount of distortion
float size = paramFloat2; // Set the amount of smoothing in the noise
float xPos = currentX + noise(xy * size); // Calculate new x position
float yPos = currentY + noise(xy * size); // Calculate new y position
vec3 colour = texture2D(u_Texture, vec2(xPos, yPos)); // Draw the new pixel value
```
 <img width="300px" src="examples/Original.jpg" /> <img width="300px" src="examples/9%20Random%20Distort.jpg" />```


















