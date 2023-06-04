precision mediump float;

uniform sampler2D u_Texture;
uniform float paramFloat1;
varying vec3 v_Position;
varying vec2 v_TexCoordinate;

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate);

    vec3 hsv = vec3(paramFloat1, 1.0, 0.5);
    vec3 rgb = hsv2rgb(hsv);

    vec3 colorMap = vec3(1.0, 1.0, 1.0);

    for (int i = 0; i < 3; i++){
        if (textureColor[i] < rgb[i]){
            colorMap[i] = 0.0;
        }
        else{
            colorMap[i] = 1.0;
        }
        textureColor[i] = textureColor[i] * colorMap[i];
    }

    gl_FragColor = textureColor;
}
