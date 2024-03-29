precision mediump float;

uniform sampler2D u_Texture;
uniform float paramFloat1;
uniform float paramFloat2;
varying vec3 v_Position;
varying vec2 v_TexCoordinate;

vec3 rgb2hsv(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}
vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    vec4 textureColor = texture2D(u_Texture, v_TexCoordinate);

    vec3 rgb = textureColor.xyz;
    vec3 hsv = rgb2hsv(rgb);

    float hue = hsv[0];
    float hueMapped = (mod(mod(hue - paramFloat1 + 1.0, 1.0) + 0.5, 1.0) - 0.5) * 2.0;

    hueMapped = sign(hueMapped) * pow(sign(hueMapped) * hueMapped, paramFloat2);

    hueMapped = mod(hueMapped / 2.0 + 1.0 + paramFloat1, 1.0);

    vec3 newHsv = vec3(hueMapped, hsv[1], hsv[2]);
    vec3 newRgb = hsv2rgb(newHsv);

    vec4 colour = vec4(newRgb, 1.0);


    gl_FragColor = colour;

}
