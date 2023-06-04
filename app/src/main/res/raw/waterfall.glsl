precision mediump float;

uniform sampler2D u_Texture;

uniform float paramFloat1;
uniform float paramFloat2;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;

void main() {

    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate);

    float y1 = 1.0 - paramFloat1;
    float y2 = max(y1 - paramFloat2, 0.0);


    float curY = v_TexCoordinate.y;
    float pixel_pos = (curY - y1) / (y2 - y1);


    if(curY < y1 && curY > y2){
        vec4 textureColor1 = texture2D(u_Texture, vec2(v_TexCoordinate.x, y1));
        vec4 textureColor2 = texture2D(u_Texture, vec2(v_TexCoordinate.x, y2));
        for(int i = 0; i < 3; i++){
            textureColor[i] = textureColor1[i] * (1.0-pixel_pos) + textureColor2[i]* pixel_pos;
        }

    }

    gl_FragColor = textureColor;
}
