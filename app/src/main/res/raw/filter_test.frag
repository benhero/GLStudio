precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_TextureUnit;
uniform float xV;
uniform float yV;

vec2 translate(vec2 srcCoord, float x, float y) {
    if (mod(srcCoord.y, 0.25) > 0.125) {
        return vec2(srcCoord.x + x, srcCoord.y + y);
    } else {
        return vec2(srcCoord.x - x, srcCoord.y + y);
    }
}

void main() {
    vec2 offsetTexCoord = translate(v_TexCoord, xV, yV);

    if (offsetTexCoord.x >= 0.0 && offsetTexCoord.x <= 1.0 &&
        offsetTexCoord.y >= 0.0 && offsetTexCoord.y <= 1.0) {
        gl_FragColor = texture2D(u_TextureUnit, offsetTexCoord);
    }
}