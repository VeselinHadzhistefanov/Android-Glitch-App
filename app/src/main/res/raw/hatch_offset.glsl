precision mediump float;

uniform sampler2D u_Texture;

uniform float paramFloat1;
uniform float paramFloat2;
uniform float paramFloat3;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;

#define PI 3.1415926538

float hatch(float x, float freq) {
    float wave = cos(x * freq * PI * 2.0);
    float value = 0.0;

    if (wave > 0.0){
        value = 1.0;
    } else {
        value = -1.0;
    }

    return value;
}

void main() {
    vec4 textureColor = vec4(0.0, 0.0, 0.0, 1.0);

    float amp = paramFloat1 * 0.2;
    float freq = paramFloat2 * 10.0;
    float xPos = v_TexCoordinate.x - 0.5 - paramFloat3;
    float yPos = v_TexCoordinate.y + hatch(xPos, freq) * amp;

    yPos = mod(yPos + 1.0, 1.0);
    textureColor = texture2D(u_Texture, vec2(v_TexCoordinate.x, yPos));

    gl_FragColor = textureColor;
}
