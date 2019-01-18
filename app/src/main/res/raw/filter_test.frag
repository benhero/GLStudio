precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_TextureUnit;
uniform float isVertical;
uniform float isHorizontal;
uniform float cloneCount;
void main() {
    vec4 source = texture2D(u_TextureUnit, v_TexCoord);
    float coordX = v_TexCoord.x;
    float coordY = v_TexCoord.y;
    if (isVertical == 1.0) {
        float width = 1.0 / cloneCount;
        float startX = (1.0 - width) / 2.0;
        coordX = mod(v_TexCoord.x, width) + startX;
    }
    if (isHorizontal == 1.0) {
        float height = 1.0 / cloneCount;
        float startY = (1.0 - height) / 2.0;
        coordY = mod(v_TexCoord.y, height) + startY;
    }
    gl_FragColor = texture2D(u_TextureUnit, vec2(coordX, coordY));
}