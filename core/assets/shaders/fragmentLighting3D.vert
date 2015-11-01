#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_uv;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

const int lc = 7;
uniform vec4 u_eyePosition;
uniform vec4 u_lightPosition[lc];
varying vec4 v_normal;
varying vec4 v_s[lc];
varying vec4 v_h[lc];
varying vec2 v_uv;

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	v_normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	v_normal = u_modelMatrix * v_normal;

	//global coordinates

	//Lighting
	for (int i = 0; i < lc; i++){
        v_s[i] = u_lightPosition[i] - position;
        vec4 v = u_eyePosition - position;
        v_h[i] = v_s[i] + v;
	}
	position = u_viewMatrix * position;

    v_uv = a_uv;
	gl_Position = u_projectionMatrix * position;
}
