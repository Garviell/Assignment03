#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 u_globalAmbient;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform vec4 u_materialEmission;
uniform float u_materialShininess;

const int start = 0;
const int lc = 2;
uniform vec4 u_lightDirection[lc];
uniform vec4 u_lightDiffuse[lc];
uniform float u_lightFocus[lc];
uniform float u_constantAttenuation[lc];
uniform float u_linearAttenuation[lc];
uniform float u_quadraticAttenuation[lc];

varying vec4 v_normal;
varying vec4 v_s[lc];
varying vec4 v_h[lc];
varying vec4 v_normal2;
varying vec4 v_s2;
varying vec4 v_h2;

void main()
{
    vec4 lightCalcColor[lc];
	for (int i = start; i < lc; ++i){
        float length_s = length(v_s[i]);
        float length_n = length(v_normal);
        float lambert = max(0, dot(v_normal, v_s[i]) / (length_n * length_s));
        float phong = max(0, dot(v_normal,v_h[i]) / (length_n * length(v_h[i])));
        vec4 diffuseColor = lambert * u_lightDiffuse[i] * u_materialDiffuse;
        vec4 specularColor = pow(phong, u_materialShininess) * u_lightDiffuse[i] * u_materialSpecular;
        float spotAttenuation = pow(max(0, dot(-v_s[i], u_lightDirection[i]) / (length_s * length(u_lightDirection[i]))), u_lightFocus[i]);
        float distanceAttenuation;
        if(u_constantAttenuation[i] == 1 && u_linearAttenuation[i] + u_quadraticAttenuation[i] <= 0.00001){
            distanceAttenuation = u_constantAttenuation[i];
        } else {
            distanceAttenuation = 1.0 / (u_constantAttenuation[i] + length_s * u_linearAttenuation[i] + pow(length_s, 2) * u_quadraticAttenuation[i]);
        }
        lightCalcColor[i] = distanceAttenuation * spotAttenuation * (diffuseColor + specularColor);
	}

	gl_FragColor =  u_globalAmbient * u_materialDiffuse + lightCalcColor[0] + lightCalcColor[1] + u_materialEmission;
}
