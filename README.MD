# Title: mix(minecraft, terraria, stardew_valley)
A little project of mine for learning LWJGL 3 and OpenGL.
Over the course of developing this little minecraft clone i have set the goal to try implementing a mix of minecraft, terraria and stardew valley into one game. 

- all "dimensions" of minecraft (overworld, nether, the end) should not be separated by a teleporting portal. they should all be seamlessly connected vial theri respective y-levels
- it should contain a wide variety of interesting and challenging opponents and bosses like terraria
- but also be chill like stardew valley. no pressure for doing chit. no forced events like in terraria (maybe being able to skip the veent via a time speedup in bed)
- some sort of automation for resources (coal, iron, diamond, etc.) as i hate to mine in minecraft (kinda ironic ik)
- but it should also be completly customizable by a "resource pack". everything that is in the resources folder should be able to be replaces by such a resource pack. textures, sounds, models, items, blocks, recipes, etc.
- real nps which may be ai controlled not just mindless stupid villagers. maybe a guide like in terraria which generates a guide procedurally via ai/

## Visuals
+ [data/screenshots](data/screenshots)
+ [data/maps](data/maps)

## Ideas / Fixes / Bugs
+ Real Physics (not just heightmap checking)
+ Shadows
+ Particles
+ Audio
+ Controller input
+ clouds like blocks which change geometry over time
+ if player reaches end of world, tp to start of world (x, y and z axis)
+ make a moon very high in the sky
+ hud (maybe use jcef as browser renderer idk)
+ inventory
+ tools
+ world saving / loading
+ Fix visual artifacts when player is at very high distances away from (0,0) -> (100000, 100000)
    * Jitter problem fix -> https://www.reddit.com/r/Unity3D/comments/fv1rjm/terrain_and_objects_flickering_when_they_are_far/
        * The second problem that could be causing your issues is the limited precision of floating point values. The way floats work is that they only have a limited number of digits that they can store before they have to round up or down. A 32-bit float can typically store between 6-9 significant digits, this means that when you move far enough away from the origin (the point at (0,0,0)) anything doing math with floats begins to round values up and down.
        * Games with large open world get around this by moving the origin (google floating origin for more on this). One way to do this in Unity would be to move every object in the scene once the character, or camera, gets too far from (0,0,0)
+ Skybox with sun visible
+ 