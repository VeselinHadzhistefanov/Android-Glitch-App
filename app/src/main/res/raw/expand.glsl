precision mediump float;

uniform sampler2D u_Texture;

uniform float paramFloat1;
uniform float paramFloat2;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;

void main() {

    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate);

    float pos = 1.0 - paramFloat1;
    float size = paramFloat2;


    float pixel_pos = v_TexCoordinate.y;

    if(pixel_pos < pos  && pixel_pos > pos - size){
        textureColor = texture2D(u_Texture, vec2(v_TexCoordinate.x, pos ));
    }


    gl_FragColor = textureColor;

}
