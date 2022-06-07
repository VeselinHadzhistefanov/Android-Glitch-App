precision mediump float;

uniform sampler2D u_Texture;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;

void main() {

    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate);

    gl_FragColor = textureColor;

}
