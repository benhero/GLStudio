precision mediump float;
varying vec2 v_TexCoord;
uniform sampler2D u_TextureUnit;
void main()
{
    vec4 pic = texture2D(u_TextureUnit, v_TexCoord);
    float gray = (pic.r + pic.g + pic.b) / 3.0f;
    gl_FragColor = vec4(gray,gray,gray,pic.a);
}