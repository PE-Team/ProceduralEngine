package pe.engine.graphics.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

import pe.engine.main.PE;
import pe.util.math.Vec3f;

public class StaticMesh3D extends Mesh {

	public StaticMesh3D(Vec3f[] vertices, int[] indices) {
		super(PE.STATIC_MESH_3D, indices.length, vertices.length, 3);

		try (MemoryStack stack = MemoryStack.stackPush()) {
			vao.use();

			FloatBuffer vertecesBuffer = stack.mallocFloat(3 * vertices.length);
			for (Vec3f vertex : vertices) {
				vertex.putInBuffer(vertecesBuffer);
			}
			vertecesBuffer.flip();

			this.meshDataLocation = vao.addVBO(3, vertecesBuffer);

			IntBuffer indecesBuffer = stack.mallocInt(indices.length);
			indecesBuffer.put(indices);
			indecesBuffer.flip();

			vao.addEBO(indecesBuffer);

			vao.unbind();
		}
	}
}
