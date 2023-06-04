precision mediump float;

uniform sampler2D u_Texture;
uniform float paramFloat1;
uniform float paramFloat2;
varying vec3 v_Position;
varying vec2 v_TexCoordinate;

#define PI 3.1415926538

void main() {
    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate);

    float size = paramFloat1;
    float position = 1.0 - paramFloat2;

    float x = v_TexCoordinate.x;
    float y = v_TexCoordinate.y;

    if (y < position && y > position - size){
        textureColor = texture2D(u_Texture, vec2(x, position));
    }

    if (y < position - size){
        textureColor = texture2D(u_Texture, vec2(x, y + size));
    }

    gl_FragColor = textureColor;
}
