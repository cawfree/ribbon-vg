# ribbon-vg

Ribbon is a high performance, hardware accelerated, platform independent signed distance vector graphics rendering library.

## About Ribbon

This API is used to convert vector image data specified via either TrueType Fonts, SVG Paths or programmatic definitions into an equivalent OpenGL Vertex Buffer Object (VBO) that can be persisted between GPU render frames. Commonly, 2D vector graphics libraries use tessellation to generate image data which must be recomputed whenever any shape attributes are changed. This commonly means that computationally-intensive tessellation occurs every render grame.

By contrast, Ribbon uses the signed-distance method. This enables VBOs to be translated, rotated, scaled and skewed in a manner that is effectively computationally-free with respect to the host CPU. This is achieved a branch-free GLSL shader compatible with both the Java and Android Dalvik runtime. 

Ribbon is entirely self sufficient, aside from boilerplate OpenGL intergration. In addition, it contains an excellent PNG Image parser which greatly outperforms existing implementations.

## Thanks

* Charles Loop and Jim Blinn, inventors of the [signed distance rendering technique](http://http.developer.nvidia.com/GPUGems3/gpugems3_ch25.html).
* Mikko Mononen over at [NanoVG](https://github.com/memononen) for his shape generation algorithms.
* Vladmirir Agafonkin over at the [Earcut](https://github.com/mapbox/earcut) Project, for his super fast, simple and succinct polygon triangulation library.

Finally, a shout out to my good friends Bart Garcia and Chao Jiang of the University of Manchester, who made studying a PhD not just bareable, but deeply enjoyable too. I'm very proud to have worked with both of you, and even prouder to call you my friends.
