precision mediump float;

uniform sampler2D u_Texture;

uniform float paramFloat1;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;


void main() {
    float blur_amt = paramFloat1*0.01;

    vec3 rgb = vec3(0.0, 0.0, 0.0);

    for (int i = 0; i < 9; i++){

        int xCoord = i - 3*(i/3) - 1;
        int yCoord = i/3 - 1;

        vec2 targetPixel;
        targetPixel[0] = v_TexCoordinate[0] + float(xCoord)*blur_amt;
        targetPixel[1] = v_TexCoordinate[1] + float(yCoord)*blur_amt;

        vec4 pixelColor = texture2D(u_Texture, targetPixel);

        rgb[0] = rgb[0] + pixelColor[0];
        rgb[1] = rgb[1] + pixelColor[1];
        rgb[2] = rgb[2] + pixelColor[2];

    }

    for (int i = 0; i < 3; i++){
        rgb[i] = rgb[i]/9.0;
    }

    vec4 pixelColour = vec4(rgb, 1.0);

    gl_FragColor = pixelColour;

}