precision mediump float;

uniform sampler2D u_Texture;

uniform float paramFloat1;
uniform float paramFloat2;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;

#define PHI 1.61803398874989484820459

float random(vec2 xy){
    return fract(tan(distance(xy*PHI, xy)*1.23456)*xy.x);
}

float noise (vec2 xy) {
    vec2 i = floor(xy);
    vec2 f = fract(xy);

    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f*f*(3.0-2.0*f);

    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}

void main() {

    float amt = paramFloat1/32.0;
    float grain =  paramFloat2 * 1000.0 + 10.0;

    vec2 pos = vec2((v_TexCoordinate-0.5)*grain);

    vec2 xy = vec2(noise(pos), noise(pos + grain));
    xy = amt*(xy*2.0 - 1.0);

    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate + xy);


    gl_FragColor = textureColor;

}
