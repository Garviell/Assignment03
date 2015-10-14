
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;
uniform vec4 u_lightPosition;

varying vec4 v_normal;
varying vec4 v_s;
varying vec4 v_h;

uniform vec4 u_lightPosition2;
varying vec4 v_normal2;
varying vec4 v_s2;
varying vec4 v_h2;

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	v_normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	v_normal = u_modelMatrix * v_normal;
	
	//global coordinates

	//Lighting

	v_s = u_lightPosition - position;
	vec4 v = u_eyePosition - position;
	v_h = v_s + v;

	v_normal2 = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	v_normal2 = u_modelMatrix * v_normal2;

	//global coordinates

	//Lighting

	v_s2 = u_lightPosition2 - position;
	vec4 v2 = u_eyePosition - position;
	v_h2 = v_s2 + v2;

	position = u_viewMatrix * position;


	gl_Position = u_projectionMatrix * position;
}