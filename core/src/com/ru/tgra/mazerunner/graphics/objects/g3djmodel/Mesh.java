package com.ru.tgra.mazerunner.graphics.objects.g3djmodel;

import java.nio.FloatBuffer;
import java.util.Vector;

public class Mesh {
	//public Vector<String> attributes;
	public FloatBuffer vertices;
	public FloatBuffer normals;

	public Mesh()
	{
		vertices = null;
		normals = null;
	}
}
