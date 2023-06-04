precision mediump float;

uniform sampler2D u_Texture;
uniform float paramFloat1;
varying vec3 v_Position;
varying vec2 v_TexCoordinate;

void main() {
    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate);

    float shift = paramFloat1 * 0.05;

    vec2 rPos = vec2(v_TexCoordinate.x + shift, v_TexCoordinate.y);
    vec4 color1 = texture2D(u_Texture, rPos);

    vec2 bPos = vec2(v_TexCoordinate.x - shift, v_TexCoordinate.y);
    vec4 color2 = texture2D(u_Texture, bPos);

    vec4 colour = vec4(color1[0], textureColor[1], color2[2], 1.0);
    gl_FragColor = colour;
}
