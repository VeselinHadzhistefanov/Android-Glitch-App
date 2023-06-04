precision mediump float;

uniform sampler2D u_Texture;

uniform float paramFloat1;
uniform float paramFloat2;
uniform float paramFloat3;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;

#define PI 3.1415926538

float triangle(float x, float p) {
    float value = (p - abs(mod(x , 2.0*p) - p)) / p;
    return value - 0.5;
}

void main() {
    vec4 textureColor = vec4(0.0, 0.0, 0.0, 1.0);

    float amp = paramFloat1 * 0.2;
    float freq = (1.0 - paramFloat2) * 0.5 + 0.1;
    float xPos = v_TexCoordinate.x - 0.5 - paramFloat3;
    float yPos = v_TexCoordinate.y + triangle(xPos, freq) * amp;

    yPos = mod(yPos + 1.0, 1.0);
    textureColor = texture2D(u_Texture, vec2(v_TexCoordinate.x, yPos));

    gl_FragColor = textureColor;
}
