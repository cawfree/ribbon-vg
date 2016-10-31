# ribbon-vg

Ribbon is a high performance, hardware accelerated, platform independent signed distance vector graphics rendering library.

Ribbon converts vector image data into an OpenGL Vertex Buffer Object (VBO) that can be persisted between render frames. Using the signed-distance method, these VBOs may be translated, rotated, scaled and skewed in a method that is effectively computationally-free with respect to the host CPU.

# Capabilities
TrueType Fonts, Rudimentary SVG Parsing and Programmatic Shape Specification.

# Coming Soon
A version which supports gradient rendering. This is already finished and is in the process of being optimised.

# Thanks

This would not be possible without the work of Charles Loop and Jim Blinn, inventors of the signed distance rendering technique.
http://http.developer.nvidia.com/GPUGems3/gpugems3_ch25.html

In addition, I would like to kindly thank Mikko Mononen over at NanoVG for his shape generation algorithms.
https://github.com/memononen

Many thanks to Vladmirir Agafonkin over at the Earcut Project, for his super fast, simple and succinct polygon triangulation library. It was a pleasure to work with you. 
https://github.com/mapbox/earcut

Finally, a big thank you to the University of Manchester for supporting both me and this project throughout. 
