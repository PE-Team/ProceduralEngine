package pe.engine.graphics.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import pe.engine.main.PE;
import pe.util.math.Vec2f;
import pe.util.shapes.Polygon;

public class StaticMesh2D extends Mesh {

	public StaticMesh2D(Vec2f[] vertices, int[] indices) {
		super(PE.STATIC_MESH_2D, indices.length, vertices.length, 2);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			vao.bind();

			FloatBuffer vertecesBuffer = stack.mallocFloat(2 * vertices.length);
			for (Vec2f vertex : vertices) {
				vertex.putInBuffer(vertecesBuffer);
			}
			vertecesBuffer.flip();

			this.meshDataLocation = vao.addVBO(2, vertecesBuffer);

			IntBuffer indecesBuffer = stack.mallocInt(indices.length);
			indecesBuffer.put(indices);
			indecesBuffer.flip();

			vao.addEBO(indecesBuffer);

			vao.unbind();
		}
	}
	
	public static StaticMesh2D fromPolygon(Polygon shape){
		return new StaticMesh2D(shape.getVertices(), shape.getIndices());
	}
}
