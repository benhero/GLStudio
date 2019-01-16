precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_TextureUnit;
uniform float uTime;

vec2 scale(vec2 srcCoord, float xScale, float yScale) {
    return vec2((srcCoord.x - 0.5) / xScale + 0.5, (srcCoord.y - 0.5) / yScale + 0.5);
}

void main() {
    float scaleValue = abs(sin(uTime / 1000.0)) + 0.5;
    vec2 offsetTexCoord = scale(v_TexCoord, scaleValue, scaleValue);
    if (offsetTexCoord.x >= 0.0 && offsetTexCoord.x <= 1.0 &&
        offsetTexCoord.y >= 0.0 && offsetTexCoord.y <= 1.0) {
        gl_FragColor = texture2D(u_TextureUnit, offsetTexCoord);
    }
}