precision mediump float;

uniform sampler2D u_Texture;
uniform vec2 iResolution;
uniform int  iFrame;
uniform float paramFloat1;
uniform float paramFloat2;

varying vec3 v_Position;
varying vec2 v_TexCoordinate;

#define PI 3.1415926538

float gscale (vec3 c) {
    return (c.r+c.g+c.b)/3.0;
}

void main() {


    float THRESHOLD = 1.0 - paramFloat1;
    float DIRECTION = paramFloat2;

    // Pixel space to texture space
    vec2 uv = gl_FragCoord.xy / iResolution.xy;

    // Alternate frames betweem -1.0 and 1.0
    float fParity = mod(float(iFrame), 2.0) * 2.0 - 1.0;

    float tanXY = sin(DIRECTION *2.0*PI) / cos(DIRECTION *2.0*PI);
    float cotanXY = cos(DIRECTION *2.0*PI) / sin(DIRECTION *2.0*PI);
    float dirCompare = 1.0;
    float xDir = 1.0;
    float yDir = 1.0;
    float swapDir = 1.0;


    //abs(cotanXY)
    if(abs(tanXY) < 1.0){

        dirCompare = (mod(floor(gl_FragCoord.x), 2.0) *2.0 - 1.0) * fParity;
        float currY = floor(tanXY * floor(gl_FragCoord.x));
        float compY = floor(tanXY * floor(gl_FragCoord.x + dirCompare));
        yDir = compY - currY;
        xDir = dirCompare;
        //swapDir = dirCompare * sign(cos(DIRECTION *2.0*PI));
        swapDir = dirCompare * sign(cos(DIRECTION *2.0*PI));
    }
    else{

        dirCompare = (mod(floor(gl_FragCoord.y), 2.0) *2.0 - 1.0) * fParity;
        float currX = floor(cotanXY * floor(gl_FragCoord.y));
        float compX = floor(cotanXY * floor(gl_FragCoord.y + dirCompare));
        xDir = compX - currX;
        yDir = dirCompare;
        swapDir = dirCompare * sign(sin(DIRECTION *2.0*PI));
    }

    // Set the direction of the swap
    vec2 dir = vec2(xDir, yDir);
    dir/= iResolution.xy;

    // Get pixels
    vec4 curr = texture2D(u_Texture, uv);
    vec4 comp = texture2D(u_Texture, uv + dir);

    float gCurr = gscale(curr.rgb);
    float gComp = gscale(comp.rgb);


    // we prevent the sort from happening on the borders
    if (uv.x + dir.x < 0.0 || uv.x + dir.x > 1.0 || uv.y + dir.y < 0.0 || uv.y + dir.y > 1.0 ) {
        gl_FragColor = curr;
        return;
    }


    // the direction of the displacement defines the order of the comparaison
    //if (dir.x*swapDir < 0.0) {
    if (swapDir < 0.0) {
        if (gCurr > THRESHOLD && gComp > gCurr) {
            gl_FragColor = comp;
        } else {
            gl_FragColor = curr;
        }
    }
    else {
        if (gComp > THRESHOLD && gCurr >= gComp) {
            gl_FragColor = comp;
        } else {
            gl_FragColor = curr;
        }
    }
}