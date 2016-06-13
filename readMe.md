# jEngine

A 3D game engine, coded in java, employing LWJGL (Light Weight Java Game Library). Supports .obj models, multitexturing, procedural terains, different light types, skybox,fog, 3rd person camera and more. 

Included are two main clases, one working with water. 


## Examples of use
Adding an object 
```java
//creates mesh object
RawModel b = OBJLoader.loadObjModel("stanfordBunny", loader);
//creates texture to be applied on mesh
ModelTexture skin = new ModelTexture(loader.loadTextures("uni"));
//setting texture parameters
skin.setReflectivity(0.5f);
skin.setShineDamper(10);
//connecting mesh and texture
TexturedModel stanfordBunny = new TexturedModel(bunny,skin);

//as a player
Player player = new Player(stanfordBunny,0,new Vector3f(0,0,0),0,180,0,1);
//as a static object
Entity bunny = new Entity(stanfordBunny,0,new Vector3f(50,terrain.getTerrainHeight(50, 20), 20),0,0,0,2);
```

## Previews
<img src="https://github.com/AndrejaKovacic/jEngine/blob/master/previews/panorama.jpg" width ="550"/>

<img src="https://github.com/AndrejaKovacic/jEngine/blob/master/previews/water.jpg" width ="550"/>



Some code snippets courtesy of [ThinMatrix]( https://twitter.com/ThinMatrix). 
