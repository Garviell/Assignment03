#ifdef GL_ES
precision mediump float;
#endif

uniform vec4 u_globalAmbient;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform vec4 u_materialEmission;
uniform float u_materialShininess;

uniform vec4 u_lightDirection;
uniform vec4 u_lightDiffuse;
uniform float u_lightFocus;
uniform float u_constantAttenuation;
uniform float u_linearAttenuation;
uniform float u_quadraticAttenuation;

uniform vec4 u_lightDirection2;
uniform vec4 u_lightDiffuse2;
uniform float u_lightFocus2;
uniform float u_constantAttenuation2;
uniform float u_linearAttenuation2;
uniform float u_quadraticAttenuation2;

varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;
varying vec4 v_normal2;
varying vec4 v_s2;
varying vec4 v_h2;

void main()
{
    float length_s = length(v_s);
    float length_n = length(v_normal);
	float lambert = max(0, dot(v_normal, v_s) / (length_n * length_s));
	float phong = max(0, dot(v_normal,v_h) / (length_n * length(v_h)));
	vec4 diffuseColor = lambert * u_lightDiffuse * u_materialDiffuse;
	vec4 specularColor = pow(phong, u_materialShininess) * u_lightDiffuse * u_materialSpecular;
	float spotAttenuation = pow(max(0, dot(-v_s, u_lightDirection) / (length_s * length(u_lightDirection))), u_lightFocus);
	float distanceAttenuation;
	if(u_constantAttenuation == 1 && u_linearAttenuation + u_quadraticAttenuation <= 0.00001){
        distanceAttenuation = u_constantAttenuation;
	} else {
	    distanceAttenuation = 1.0 / (u_constantAttenuation + length_s * u_linearAttenuation + pow(length_s, 2) * u_quadraticAttenuation);
	}
	vec4 lightCalcColor1 = distanceAttenuation * spotAttenuation * (diffuseColor + specularColor);

    length_s = length(v_s2);
    length_n = length(v_normal2);
	lambert = max(0, dot(v_normal2, v_s2) / (length_n * length_s));
	phong = max(0, dot(v_normal2,v_h2) / (length_n * length(v_h2)));
	diffuseColor = lambert * u_lightDiffuse2 * u_materialDiffuse;
	specularColor = pow(phong, u_materialShininess) * u_lightDiffuse * u_materialSpecular;
	spotAttenuation = pow(max(0, dot(-v_s2, u_lightDirection2) / (length_s * length(u_lightDirection2))), u_lightFocus2);
	if(u_constantAttenuation2 == 1 && u_linearAttenuation2 + u_quadraticAttenuation2 <= 0.00001){
        distanceAttenuation = u_constantAttenuation2;
	} else {
	    distanceAttenuation = 1.0 / (u_constantAttenuation2 + length_s * u_linearAttenuation2 + pow(length_s, 2) * u_quadraticAttenuation2);
	}
	vec4 lightCalcColor2 = distanceAttenuation * spotAttenuation * (diffuseColor + specularColor);

	gl_FragColor =  u_globalAmbient * u_materialDiffuse + lightCalcColor1 + lightCalcColor2 + u_materialEmission;
}
