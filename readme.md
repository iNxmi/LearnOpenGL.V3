# Voxel Game (not then name)
A little project of mine for learning LWJGL 3 and OpenGL

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