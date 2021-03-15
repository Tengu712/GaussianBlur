#version 330

#define PI 3.141592
#define VAR 25.0

in vec4 colorOut;
in vec2 uvOut;

uniform int gaussian;
uniform sampler2D texture;

out vec4 color;

float getGauss(float x){
    return exp(-0.5*x*x/VAR)/sqrt(2.0*PI*VAR);
}

vec4 getTex(vec2 uv){
    return texture2D(texture, uv);
}

void main (void) {
    vec4 col = vec4(0.0);
    if(gaussian==1){
        int r = 40;
        float flagX = 1.0 / textureSize(texture, 0).x;
        float flagY = 1.0 / textureSize(texture, 0).y;
        for(int i=-r/2; i<=r/2; ++i){
            float gauss = getGauss(i);
            col += getTex(uvOut + vec2(i*flagX,0)) * gauss;
            col += getTex(uvOut + vec2(0,i*flagY)) * gauss;
        }
        col /= 2.0;
        
    }else{
        col = getTex(uvOut);
    }
    color = col * colorOut;
}