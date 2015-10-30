package com.ru.tgra.mazerunner.graphics.objects.g3djmodel;


import com.ru.tgra.mazerunner.util.Point3D;
import com.ru.tgra.mazerunner.util.Quaternion;
import com.ru.tgra.mazerunner.util.Vector3D;

import java.util.Vector;

public class MeshModelNode {
	public String id;
	public Quaternion rotation;
	public Vector3D scale;
	public Point3D translation;
	public Vector<MeshModelNodePart> parts;

	public MeshModelNode()
	{
		rotation = new Quaternion();
		scale = new Vector3D(1.0f, 1.0f, 1.0f);
		translation = new Point3D(0.0f, 0.0f, 0.0f);
		parts = new Vector<MeshModelNodePart>();
	}
}
