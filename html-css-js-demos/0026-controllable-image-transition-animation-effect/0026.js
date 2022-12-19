// bas.js
THREE.BAS = {};

THREE.BAS.ShaderChunk = {};

THREE.BAS.ShaderChunk["animation_time"] =
    "float tDelay = aAnimation.x;\nfloat tDuration = aAnimation.y;\nfloat tTime = clamp(uTime - tDelay, 0.0, tDuration);\nfloat tProgress = ease(tTime, 0.0, 1.0, tDuration);\n";

THREE.BAS.ShaderChunk["catmull-rom"] =
    "vec3 catmullRom(vec3 p0, vec3 p1, vec3 p2, vec3 p3, float t)\n{\n    vec3 v0 = (p2 - p0) * 0.5;\n    vec3 v1 = (p3 - p1) * 0.5;\n    float t2 = t * t;\n    float t3 = t * t * t;\n\n    return vec3((2.0 * p1 - 2.0 * p2 + v0 + v1) * t3 + (-3.0 * p1 + 3.0 * p2 - 2.0 * v0 - v1) * t2 + v0 * t + p1);\n}\n\nvec3 catmullRom(vec3 p0, vec3 p1, vec3 p2, vec3 p3, vec2 c, float t)\n{\n    vec3 v0 = (p2 - p0) * c.x;\n    vec3 v1 = (p3 - p1) * c.y;\n    float t2 = t * t;\n    float t3 = t * t * t;\n\n    return vec3((2.0 * p1 - 2.0 * p2 + v0 + v1) * t3 + (-3.0 * p1 + 3.0 * p2 - 2.0 * v0 - v1) * t2 + v0 * t + p1);\n}\n\nfloat catmullRom(float p0, float p1, float p2, float p3, float t)\n{\n    float v0 = (p2 - p0) * 0.5;\n    float v1 = (p3 - p1) * 0.5;\n    float t2 = t * t;\n    float t3 = t * t * t;\n\n    return float((2.0 * p1 - 2.0 * p2 + v0 + v1) * t3 + (-3.0 * p1 + 3.0 * p2 - 2.0 * v0 - v1) * t2 + v0 * t + p1);\n}\n\nfloat catmullRom(float p0, float p1, float p2, float p3, vec2 c, float t)\n{\n    float v0 = (p2 - p0) * c.x;\n    float v1 = (p3 - p1) * c.y;\n    float t2 = t * t;\n    float t3 = t * t * t;\n\n    return float((2.0 * p1 - 2.0 * p2 + v0 + v1) * t3 + (-3.0 * p1 + 3.0 * p2 - 2.0 * v0 - v1) * t2 + v0 * t + p1);\n}\n";

THREE.BAS.ShaderChunk["cubic_bezier"] =
    "vec3 cubicBezier(vec3 p0, vec3 c0, vec3 c1, vec3 p1, float t)\n{\n    vec3 tp;\n    float tn = 1.0 - t;\n\n    tp.xyz = tn * tn * tn * p0.xyz + 3.0 * tn * tn * t * c0.xyz + 3.0 * tn * t * t * c1.xyz + t * t * t * p1.xyz;\n\n    return tp;\n}\n";

THREE.BAS.ShaderChunk["ease_in_cubic"] =
    "float ease(float t, float b, float c, float d) {\n  return c*(t/=d)*t*t + b;\n}\n";

THREE.BAS.ShaderChunk["ease_in_out_cubic"] =
    "float ease(float t, float b, float c, float d) {\n  if ((t/=d/2.0) < 1.0) return c/2.0*t*t*t + b;\n  return c/2.0*((t-=2.0)*t*t + 2.0) + b;\n}\n";

THREE.BAS.ShaderChunk["ease_in_quad"] =
    "float ease(float t, float b, float c, float d) {\n  return c*(t/=d)*t + b;\n}\n";

THREE.BAS.ShaderChunk["ease_out_back"] =
    "float ease(float t, float b, float c, float d) {\n  float s = 1.70158;\n  return c*((t=t/d-1.0)*t*((s+1.0)*t + s) + 1.0) + b;\n}\n\nfloat ease(float t, float b, float c, float d, float s) {\n  return c*((t=t/d-1.0)*t*((s+1.0)*t + s) + 1.0) + b;\n}\n";

THREE.BAS.ShaderChunk["ease_out_cubic"] =
    "float ease(float t, float b, float c, float d) {\n  return c*((t=t/d - 1.0)*t*t + 1.0) + b;\n}\n";

THREE.BAS.ShaderChunk["quaternion_rotation"] =
    "vec3 rotateVector(vec4 q, vec3 v)\n{\n    return v + 2.0 * cross(q.xyz, cross(q.xyz, v) + q.w * v);\n}\n\nvec4 quatFromAxisAngle(vec3 axis, float angle)\n{\n    float halfAngle = angle * 0.5;\n    return vec4(axis.xyz * sin(halfAngle), cos(halfAngle));\n}\n";

THREE.BAS.Utils = {
    separateFaces: function (geometry) {
        var vertices = [];

        for (var i = 0, il = geometry.faces.length; i < il; i++) {
            var n = vertices.length;

            var face = geometry.faces[i];

            var a = face.a;
            var b = face.b;
            var c = face.c;

            var va = geometry.vertices[a];
            var vb = geometry.vertices[b];
            var vc = geometry.vertices[c];

            vertices.push(va.clone());
            vertices.push(vb.clone());
            vertices.push(vc.clone());

            face.a = n;
            face.b = n + 1;
            face.c = n + 2;
        }

        geometry.vertices = vertices;
        delete geometry.__tmpVertices;
    },
    tessellate: function (geometry, maxEdgeLength) {
        var edge;

        var faces = [];
        var faceVertexUvs = [];
        var maxEdgeLengthSquared = maxEdgeLength * maxEdgeLength;

        for (var i = 0, il = geometry.faceVertexUvs.length; i < il; i++) {
            faceVertexUvs[i] = [];
        }

        for (var i = 0, il = geometry.faces.length; i < il; i++) {
            var face = geometry.faces[i];

            if (face instanceof THREE.Face3) {
                var a = face.a;
                var b = face.b;
                var c = face.c;

                var va = geometry.vertices[a];
                var vb = geometry.vertices[b];
                var vc = geometry.vertices[c];

                var dab = va.distanceToSquared(vb);
                var dbc = vb.distanceToSquared(vc);
                var dac = va.distanceToSquared(vc);

                if (
                    dab > maxEdgeLengthSquared ||
                    dbc > maxEdgeLengthSquared ||
                    dac > maxEdgeLengthSquared
                ) {
                    var m = geometry.vertices.length;

                    var triA = face.clone();
                    var triB = face.clone();

                    if (dab >= dbc && dab >= dac) {
                        var vm = va.clone();
                        vm.lerp(vb, 0.5);

                        triA.a = a;
                        triA.b = m;
                        triA.c = c;

                        triB.a = m;
                        triB.b = b;
                        triB.c = c;

                        if (face.vertexNormals.length === 3) {
                            var vnm = face.vertexNormals[0].clone();
                            vnm.lerp(face.vertexNormals[1], 0.5);

                            triA.vertexNormals[1].copy(vnm);
                            triB.vertexNormals[0].copy(vnm);
                        }

                        if (face.vertexColors.length === 3) {
                            var vcm = face.vertexColors[0].clone();
                            vcm.lerp(face.vertexColors[1], 0.5);

                            triA.vertexColors[1].copy(vcm);
                            triB.vertexColors[0].copy(vcm);
                        }

                        edge = 0;
                    } else if (dbc >= dab && dbc >= dac) {
                        var vm = vb.clone();
                        vm.lerp(vc, 0.5);

                        triA.a = a;
                        triA.b = b;
                        triA.c = m;

                        triB.a = m;
                        triB.b = c;
                        triB.c = a;

                        if (face.vertexNormals.length === 3) {
                            var vnm = face.vertexNormals[1].clone();
                            vnm.lerp(face.vertexNormals[2], 0.5);

                            triA.vertexNormals[2].copy(vnm);

                            triB.vertexNormals[0].copy(vnm);
                            triB.vertexNormals[1].copy(face.vertexNormals[2]);
                            triB.vertexNormals[2].copy(face.vertexNormals[0]);
                        }

                        if (face.vertexColors.length === 3) {
                            var vcm = face.vertexColors[1].clone();
                            vcm.lerp(face.vertexColors[2], 0.5);

                            triA.vertexColors[2].copy(vcm);

                            triB.vertexColors[0].copy(vcm);
                            triB.vertexColors[1].copy(face.vertexColors[2]);
                            triB.vertexColors[2].copy(face.vertexColors[0]);
                        }

                        edge = 1;
                    } else {
                        var vm = va.clone();
                        vm.lerp(vc, 0.5);

                        triA.a = a;
                        triA.b = b;
                        triA.c = m;

                        triB.a = m;
                        triB.b = b;
                        triB.c = c;

                        if (face.vertexNormals.length === 3) {
                            var vnm = face.vertexNormals[0].clone();
                            vnm.lerp(face.vertexNormals[2], 0.5);

                            triA.vertexNormals[2].copy(vnm);
                            triB.vertexNormals[0].copy(vnm);
                        }

                        if (face.vertexColors.length === 3) {
                            var vcm = face.vertexColors[0].clone();
                            vcm.lerp(face.vertexColors[2], 0.5);

                            triA.vertexColors[2].copy(vcm);
                            triB.vertexColors[0].copy(vcm);
                        }

                        edge = 2;
                    }

                    faces.push(triA, triB);
                    geometry.vertices.push(vm);

                    for (
                        var j = 0, jl = geometry.faceVertexUvs.length;
                        j < jl;
                        j++
                    ) {
                        if (geometry.faceVertexUvs[j].length) {
                            var uvs = geometry.faceVertexUvs[j][i];

                            var uvA = uvs[0];
                            var uvB = uvs[1];
                            var uvC = uvs[2];

                            // AB

                            if (edge === 0) {
                                var uvM = uvA.clone();
                                uvM.lerp(uvB, 0.5);

                                var uvsTriA = [
                                    uvA.clone(),
                                    uvM.clone(),
                                    uvC.clone(),
                                ];
                                var uvsTriB = [
                                    uvM.clone(),
                                    uvB.clone(),
                                    uvC.clone(),
                                ];

                                // BC
                            } else if (edge === 1) {
                                var uvM = uvB.clone();
                                uvM.lerp(uvC, 0.5);

                                var uvsTriA = [
                                    uvA.clone(),
                                    uvB.clone(),
                                    uvM.clone(),
                                ];
                                var uvsTriB = [
                                    uvM.clone(),
                                    uvC.clone(),
                                    uvA.clone(),
                                ];

                                // AC
                            } else {
                                var uvM = uvA.clone();
                                uvM.lerp(uvC, 0.5);

                                var uvsTriA = [
                                    uvA.clone(),
                                    uvB.clone(),
                                    uvM.clone(),
                                ];
                                var uvsTriB = [
                                    uvM.clone(),
                                    uvB.clone(),
                                    uvC.clone(),
                                ];
                            }

                            faceVertexUvs[j].push(uvsTriA, uvsTriB);
                        }
                    }
                } else {
                    faces.push(face);

                    for (
                        var j = 0, jl = geometry.faceVertexUvs.length;
                        j < jl;
                        j++
                    ) {
                        faceVertexUvs[j].push(geometry.faceVertexUvs[j][i]);
                    }
                }
            }
        }

        geometry.faces = faces;
        geometry.faceVertexUvs = faceVertexUvs;
    },
    tessellateRepeat: function (geometry, maxEdgeLength, times) {
        for (var i = 0; i < times; i++) {
            THREE.BAS.Utils.tessellate(geometry, maxEdgeLength);
        }
    },
    subdivide: function (geometry, subdivisions) {
        var WARNINGS = !true; // Set to true for development
        var ABC = ["a", "b", "c"];

        while (subdivisions-- > 0) {
            smooth(geometry);
        }

        delete geometry.__tmpVertices;
        geometry.computeFaceNormals();
        geometry.computeVertexNormals();

        function getEdge(a, b, map) {
            var vertexIndexA = Math.min(a, b);
            var vertexIndexB = Math.max(a, b);

            var key = vertexIndexA + "_" + vertexIndexB;

            return map[key];
        }

        function processEdge(a, b, vertices, map, face, metaVertices) {
            var vertexIndexA = Math.min(a, b);
            var vertexIndexB = Math.max(a, b);

            var key = vertexIndexA + "_" + vertexIndexB;

            var edge;

            if (key in map) {
                edge = map[key];
            } else {
                var vertexA = vertices[vertexIndexA];
                var vertexB = vertices[vertexIndexB];

                edge = {
                    a: vertexA, // pointer reference
                    b: vertexB,
                    newEdge: null,
                    // aIndex: a, // numbered reference
                    // bIndex: b,
                    faces: [], // pointers to face
                };

                map[key] = edge;
            }

            edge.faces.push(face);

            metaVertices[a].edges.push(edge);
            metaVertices[b].edges.push(edge);
        }

        function generateLookups(vertices, faces, metaVertices, edges) {
            var i, il, face, edge;

            for (i = 0, il = vertices.length; i < il; i++) {
                metaVertices[i] = { edges: [] };
            }

            for (i = 0, il = faces.length; i < il; i++) {
                face = faces[i];

                processEdge(
                    face.a,
                    face.b,
                    vertices,
                    edges,
                    face,
                    metaVertices
                );
                processEdge(
                    face.b,
                    face.c,
                    vertices,
                    edges,
                    face,
                    metaVertices
                );
                processEdge(
                    face.c,
                    face.a,
                    vertices,
                    edges,
                    face,
                    metaVertices
                );
            }
        }

        function newFace(newFaces, a, b, c) {
            newFaces.push(new THREE.Face3(a, b, c));
        }

        /////////////////////////////

        // Performs one iteration of Subdivision
        function smooth(geometry) {
            var tmp = new THREE.Vector3();

            var oldVertices, oldFaces;
            var newVertices, newFaces; // newUVs = [];

            var n, l, i, il, j, k;
            var metaVertices, sourceEdges;

            // new stuff.
            var sourceEdges, newEdgeVertices, newSourceVertices;

            oldVertices = geometry.vertices; // { x, y, z}
            oldFaces = geometry.faces; // { a: oldVertex1, b: oldVertex2, c: oldVertex3 }

            /******************************************************
             *
             * Step 0: Preprocess Geometry to Generate edges Lookup
             *
             *******************************************************/

            metaVertices = new Array(oldVertices.length);
            sourceEdges = {}; // Edge => { oldVertex1, oldVertex2, faces[]  }

            generateLookups(oldVertices, oldFaces, metaVertices, sourceEdges);

            /******************************************************
             *
             *  Step 1.
             *  For each edge, create a new Edge Vertex,
             *  then position it.
             *
             *******************************************************/

            newEdgeVertices = [];
            var other, currentEdge, newEdge, face;
            var edgeVertexWeight, adjacentVertexWeight, connectedFaces;

            for (i in sourceEdges) {
                currentEdge = sourceEdges[i];
                newEdge = new THREE.Vector3();

                edgeVertexWeight = 3 / 8;
                adjacentVertexWeight = 1 / 8;

                connectedFaces = currentEdge.faces.length;

                // check how many linked faces. 2 should be correct.
                if (connectedFaces != 2) {
                    // if length is not 2, handle condition
                    edgeVertexWeight = 0.5;
                    adjacentVertexWeight = 0;

                    if (connectedFaces != 1) {
                        if (WARNINGS)
                            console.warn(
                                "Subdivision Modifier: Number of connected faces != 2, is: ",
                                connectedFaces,
                                currentEdge
                            );
                    }
                }

                newEdge
                    .addVectors(currentEdge.a, currentEdge.b)
                    .multiplyScalar(edgeVertexWeight);

                tmp.set(0, 0, 0);

                for (j = 0; j < connectedFaces; j++) {
                    face = currentEdge.faces[j];

                    for (k = 0; k < 3; k++) {
                        other = oldVertices[face[ABC[k]]];
                        if (other !== currentEdge.a && other !== currentEdge.b)
                            break;
                    }

                    tmp.add(other);
                }

                tmp.multiplyScalar(adjacentVertexWeight);
                newEdge.add(tmp);

                currentEdge.newEdge = newEdgeVertices.length;
                newEdgeVertices.push(newEdge);

                // console.log(currentEdge, newEdge);
            }

            /******************************************************
             *
             *  Step 2.
             *  Reposition each source vertices.
             *
             *******************************************************/

            var beta, sourceVertexWeight, connectingVertexWeight;
            var connectingEdge, connectingEdges, oldVertex, newSourceVertex;
            newSourceVertices = [];

            for (i = 0, il = oldVertices.length; i < il; i++) {
                oldVertex = oldVertices[i];

                // find all connecting edges (using lookupTable)
                connectingEdges = metaVertices[i].edges;
                n = connectingEdges.length;
                beta;

                if (n == 3) {
                    beta = 3 / 16;
                } else if (n > 3) {
                    beta = 3 / (8 * n); // Warren's modified formula
                }

                // Loop's original beta formula
                // beta = 1 / n * ( 5/8 - Math.pow( 3/8 + 1/4 * Math.cos( 2 * Math. PI / n ), 2) );

                sourceVertexWeight = 1 - n * beta;
                connectingVertexWeight = beta;

                if (n <= 2) {
                    // crease and boundary rules
                    // console.warn('crease and boundary rules');

                    if (n == 2) {
                        if (WARNINGS)
                            console.warn("2 connecting edges", connectingEdges);
                        sourceVertexWeight = 3 / 4;
                        connectingVertexWeight = 1 / 8;

                        // sourceVertexWeight = 1;
                        // connectingVertexWeight = 0;
                    } else if (n == 1) {
                        if (WARNINGS) console.warn("only 1 connecting edge");
                    } else if (n == 0) {
                        if (WARNINGS) console.warn("0 connecting edges");
                    }
                }

                newSourceVertex = oldVertex
                    .clone()
                    .multiplyScalar(sourceVertexWeight);

                tmp.set(0, 0, 0);

                for (j = 0; j < n; j++) {
                    connectingEdge = connectingEdges[j];
                    other =
                        connectingEdge.a !== oldVertex
                            ? connectingEdge.a
                            : connectingEdge.b;
                    tmp.add(other);
                }

                tmp.multiplyScalar(connectingVertexWeight);
                newSourceVertex.add(tmp);

                newSourceVertices.push(newSourceVertex);
            }

            /******************************************************
             *
             *  Step 3.
             *  Generate Faces between source vertecies
             *  and edge vertices.
             *
             *******************************************************/

            newVertices = newSourceVertices.concat(newEdgeVertices);
            var sl = newSourceVertices.length,
                edge1,
                edge2,
                edge3;
            newFaces = [];

            for (i = 0, il = oldFaces.length; i < il; i++) {
                face = oldFaces[i];

                // find the 3 new edges vertex of each old face

                edge1 = getEdge(face.a, face.b, sourceEdges).newEdge + sl;
                edge2 = getEdge(face.b, face.c, sourceEdges).newEdge + sl;
                edge3 = getEdge(face.c, face.a, sourceEdges).newEdge + sl;

                // create 4 faces.

                newFace(newFaces, edge1, edge2, edge3);
                newFace(newFaces, face.a, edge1, edge3);
                newFace(newFaces, face.b, edge2, edge1);
                newFace(newFaces, face.c, edge3, edge2);
            }

            // Overwrite old arrays
            geometry.vertices = newVertices;
            geometry.faces = newFaces;

            // console.log('done');
        }
    },

    computeCentroid: (function () {
        var v = new THREE.Vector3();

        return function (geometry, face) {
            var a = geometry.vertices[face.a],
                b = geometry.vertices[face.b],
                c = geometry.vertices[face.c];

            v.x = (a.x + b.x + c.x) / 3;
            v.y = (a.y + b.y + c.y) / 3;
            v.z = (a.z + b.z + c.z) / 3;

            return v;
        };
    })(),
};
THREE.BAS.ModelBufferGeometry = function (model) {
    THREE.BufferGeometry.call(this);

    this.modelGeometry = model;
    this.faceCount = this.modelGeometry.faces.length;
    this.vertexCount = this.modelGeometry.vertices.length;

    this.bufferIndices();
    this.bufferPositions();
};
THREE.BAS.ModelBufferGeometry.prototype = Object.create(
    THREE.BufferGeometry.prototype
);
THREE.BAS.ModelBufferGeometry.prototype.constructor =
    THREE.BAS.ModelBufferGeometry;

THREE.BAS.ModelBufferGeometry.prototype.bufferIndices = function () {
    var indexBuffer = new Uint32Array(this.faceCount * 3);

    this.setIndex(new THREE.BufferAttribute(indexBuffer, 1));

    for (var i = 0, offset = 0; i < this.faceCount; i++, offset += 3) {
        var face = this.modelGeometry.faces[i];

        indexBuffer[offset] = face.a;
        indexBuffer[offset + 1] = face.b;
        indexBuffer[offset + 2] = face.c;
    }
};

THREE.BAS.ModelBufferGeometry.prototype.bufferPositions = function () {
    var positionBuffer = this.createAttribute("position", 3).array;

    for (var i = 0, offset = 0; i < this.vertexCount; i++, offset += 3) {
        var vertex = this.modelGeometry.vertices[i];

        positionBuffer[offset] = vertex.x;
        positionBuffer[offset + 1] = vertex.y;
        positionBuffer[offset + 2] = vertex.z;
    }
};

THREE.BAS.ModelBufferGeometry.prototype.bufferUVs = function () {
    var uvBuffer = this.createAttribute("uv", 2).array;

    for (var i = 0; i < this.faceCount; i++) {
        var face = this.modelGeometry.faces[i];
        var uv;

        uv = this.modelGeometry.faceVertexUvs[0][i][0];
        uvBuffer[face.a * 2] = uv.x;
        uvBuffer[face.a * 2 + 1] = uv.y;

        uv = this.modelGeometry.faceVertexUvs[0][i][1];
        uvBuffer[face.b * 2] = uv.x;
        uvBuffer[face.b * 2 + 1] = uv.y;

        uv = this.modelGeometry.faceVertexUvs[0][i][2];
        uvBuffer[face.c * 2] = uv.x;
        uvBuffer[face.c * 2 + 1] = uv.y;
    }
};

THREE.BAS.ModelBufferGeometry.prototype.createAttribute = function (
    name,
    itemSize
) {
    var buffer = new Float32Array(this.vertexCount * itemSize);
    var attribute = new THREE.BufferAttribute(buffer, itemSize);

    this.addAttribute(name, attribute);

    return attribute;
};

THREE.BAS.PrefabBufferGeometry = function (prefab, count) {
    THREE.BufferGeometry.call(this);

    this.prefabGeometry = prefab;
    this.prefabCount = count;
    this.prefabVertexCount = prefab.vertices.length;

    this.bufferIndices();
    this.bufferPositions();
};
THREE.BAS.PrefabBufferGeometry.prototype = Object.create(
    THREE.BufferGeometry.prototype
);
THREE.BAS.PrefabBufferGeometry.prototype.constructor =
    THREE.BAS.PrefabBufferGeometry;

THREE.BAS.PrefabBufferGeometry.prototype.bufferIndices = function () {
    var prefabFaceCount = this.prefabGeometry.faces.length;
    var prefabIndexCount = this.prefabGeometry.faces.length * 3;
    var prefabIndices = [];

    for (var h = 0; h < prefabFaceCount; h++) {
        var face = this.prefabGeometry.faces[h];
        prefabIndices.push(face.a, face.b, face.c);
    }

    var indexBuffer = new Uint32Array(this.prefabCount * prefabIndexCount);

    this.setIndex(new THREE.BufferAttribute(indexBuffer, 1));

    for (var i = 0; i < this.prefabCount; i++) {
        for (var k = 0; k < prefabIndexCount; k++) {
            indexBuffer[i * prefabIndexCount + k] =
                prefabIndices[k] + i * this.prefabVertexCount;
        }
    }
};

THREE.BAS.PrefabBufferGeometry.prototype.bufferPositions = function () {
    var positionBuffer = this.createAttribute("position", 3).array;

    for (var i = 0, offset = 0; i < this.prefabCount; i++) {
        for (var j = 0; j < this.prefabVertexCount; j++, offset += 3) {
            var prefabVertex = this.prefabGeometry.vertices[j];

            positionBuffer[offset] = prefabVertex.x;
            positionBuffer[offset + 1] = prefabVertex.y;
            positionBuffer[offset + 2] = prefabVertex.z;
        }
    }
};

// todo test
// THREE.BAS.PrefabBufferGeometry.prototype.bufferUvs = function () {
//     var prefabFaceCount = this.prefabGeometry.faces.length;
//     var prefabVertexCount = (this.prefabVertexCount =
//         this.prefabGeometry.vertices.length);
//     var prefabUvs = [];

//     for (var h = 0; h < prefabFaceCount; h++) {
//         var face = this.prefabGeometry.faces[h];
//         var uv = this.prefabGeometry.faceVertexUvs[0][h];

//         prefabUvs[face.a] = uv[0];
//         prefabUvs[face.b] = uv[1];
//         prefabUvs[face.c] = uv[2];
//     }

//     var uvBuffer = this.createAttribute("uv", 2);

//     for (var i = 0, offset = 0; i < this.prefabCount; i++) {
//         for (var j = 0; j < prefabVertexCount; j++, offset += 2) {
//             var prefabUv = prefabUvs[j];

//             uvBuffer.array[offset] = prefabUv.x;
//             uvBuffer.array[offset + 1] = prefabUv.y;
//         }
//     }
// };

/**
 * based on BufferGeometry.computeVertexNormals
 * calculate vertex normals for a prefab, and repeat the data in the normal buffer
 */
THREE.BAS.PrefabBufferGeometry.prototype.computeVertexNormals = function () {
    var index = this.index;
    var attributes = this.attributes;
    var positions = attributes.position.array;

    if (attributes.normal === undefined) {
        this.addAttribute(
            "normal",
            new THREE.BufferAttribute(new Float32Array(positions.length), 3)
        );
    }

    var normals = attributes.normal.array;

    var vA,
        vB,
        vC,
        pA = new THREE.Vector3(),
        pB = new THREE.Vector3(),
        pC = new THREE.Vector3(),
        cb = new THREE.Vector3(),
        ab = new THREE.Vector3();

    var indices = index.array;
    var prefabIndexCount = this.prefabGeometry.faces.length * 3;

    for (var i = 0; i < prefabIndexCount; i += 3) {
        vA = indices[i + 0] * 3;
        vB = indices[i + 1] * 3;
        vC = indices[i + 2] * 3;

        pA.fromArray(positions, vA);
        pB.fromArray(positions, vB);
        pC.fromArray(positions, vC);

        cb.subVectors(pC, pB);
        ab.subVectors(pA, pB);
        cb.cross(ab);

        normals[vA] += cb.x;
        normals[vA + 1] += cb.y;
        normals[vA + 2] += cb.z;

        normals[vB] += cb.x;
        normals[vB + 1] += cb.y;
        normals[vB + 2] += cb.z;

        normals[vC] += cb.x;
        normals[vC + 1] += cb.y;
        normals[vC + 2] += cb.z;
    }

    for (var j = 1; j < this.prefabCount; j++) {
        for (var k = 0; k < prefabIndexCount; k++) {
            normals[j * prefabIndexCount + k] = normals[k];
        }
    }

    this.normalizeNormals();

    attributes.normal.needsUpdate = true;
};

THREE.BAS.PrefabBufferGeometry.prototype.createAttribute = function (
    name,
    itemSize,
    factory
) {
    var buffer = new Float32Array(
        this.prefabCount * this.prefabVertexCount * itemSize
    );
    var attribute = new THREE.BufferAttribute(buffer, itemSize);

    this.addAttribute(name, attribute);

    if (factory) {
        for (var i = 0, offset = 0; i < this.prefabCount; i++) {
            var r = factory(i, this.prefabCount);

            for (var j = 0; j < this.prefabVertexCount; j++) {
                for (var k = 0; k < itemSize; k++) {
                    buffer[offset++] = typeof r === "number" ? r : r[k];
                }
            }
        }
    }

    return attribute;
};

THREE.BAS.PrefabBufferGeometry.prototype.setAttribute4 = function (name, data) {
    var offset = 0;
    var array = this.geometry.attributes[name].array;
    var i, j;

    for (i = 0; i < data.length; i++) {
        var v = data[i];

        for (j = 0; j < this.prefabVertexCount; j++) {
            array[offset++] = v.x;
            array[offset++] = v.y;
            array[offset++] = v.z;
            array[offset++] = v.w;
        }
    }

    this.geometry.attributes[name].needsUpdate = true;
};
THREE.BAS.PrefabBufferGeometry.prototype.setAttribute3 = function (name, data) {
    var offset = 0;
    var array = this.geometry.attributes[name].array;
    var i, j;

    for (i = 0; i < data.length; i++) {
        var v = data[i];

        for (j = 0; j < this.prefabVertexCount; j++) {
            array[offset++] = v.x;
            array[offset++] = v.y;
            array[offset++] = v.z;
        }
    }

    this.geometry.attributes[name].needsUpdate = true;
};
THREE.BAS.PrefabBufferGeometry.prototype.setAttribute2 = function (name, data) {
    var offset = 0;
    var array = this.geometry.attributes[name].array;
    var i, j;

    for (i = 0; i < this.prefabCount; i++) {
        var v = data[i];

        for (j = 0; j < this.prefabVertexCount; j++) {
            array[offset++] = v.x;
            array[offset++] = v.y;
        }
    }

    this.geometry.attributes[name].needsUpdate = true;
};

THREE.BAS.BaseAnimationMaterial = function (parameters) {
    THREE.ShaderMaterial.call(this);

    this.shaderFunctions = [];
    this.shaderParameters = [];
    this.shaderVertexInit = [];
    this.shaderTransformNormal = [];
    this.shaderTransformPosition = [];

    this.setValues(parameters);
};
THREE.BAS.BaseAnimationMaterial.prototype = Object.create(
    THREE.ShaderMaterial.prototype
);
THREE.BAS.BaseAnimationMaterial.prototype.constructor =
    THREE.BAS.BaseAnimationMaterial;

// abstract
THREE.BAS.BaseAnimationMaterial.prototype._concatVertexShader = function () {
    return "";
};

THREE.BAS.BaseAnimationMaterial.prototype._concatFunctions = function () {
    return this.shaderFunctions.join("\n");
};
THREE.BAS.BaseAnimationMaterial.prototype._concatParameters = function () {
    return this.shaderParameters.join("\n");
};
THREE.BAS.BaseAnimationMaterial.prototype._concatVertexInit = function () {
    return this.shaderVertexInit.join("\n");
};
THREE.BAS.BaseAnimationMaterial.prototype._concatTransformNormal = function () {
    return this.shaderTransformNormal.join("\n");
};
THREE.BAS.BaseAnimationMaterial.prototype._concatTransformPosition =
    function () {
        return this.shaderTransformPosition.join("\n");
    };

THREE.BAS.BaseAnimationMaterial.prototype.setUniformValues = function (values) {
    for (var key in values) {
        if (key in this.uniforms) {
            var uniform = this.uniforms[key];
            var value = values[key];

            // todo add matrix uniform types
            switch (uniform.type) {
                case "c": // color
                    uniform.value.set(value);
                    break;
                case "v2": // vectors
                case "v3":
                case "v4":
                    uniform.value.copy(value);
                    break;
                case "f": // float
                case "t": // texture
                default:
                    uniform.value = value;
            }
        }
    }
};

THREE.BAS.BasicAnimationMaterial = function (parameters, uniformValues) {
    THREE.BAS.BaseAnimationMaterial.call(this, parameters);

    var basicShader = THREE.ShaderLib["basic"];

    this.uniforms = THREE.UniformsUtils.merge([
        basicShader.uniforms,
        this.uniforms,
    ]);
    this.lights = false;
    this.vertexShader = this._concatVertexShader();
    this.fragmentShader = basicShader.fragmentShader;

    // todo add missing default defines
    uniformValues.map && (this.defines["USE_MAP"] = "");
    uniformValues.normalMap && (this.defines["USE_NORMALMAP"] = "");

    this.setUniformValues(uniformValues);
};
THREE.BAS.BasicAnimationMaterial.prototype = Object.create(
    THREE.BAS.BaseAnimationMaterial.prototype
);
THREE.BAS.BasicAnimationMaterial.prototype.constructor =
    THREE.BAS.BasicAnimationMaterial;

THREE.BAS.BasicAnimationMaterial.prototype._concatVertexShader = function () {
    // based on THREE.ShaderLib.phong
    return [
        THREE.ShaderChunk["common"],
        THREE.ShaderChunk["uv_pars_vertex"],
        THREE.ShaderChunk["uv2_pars_vertex"],
        THREE.ShaderChunk["envmap_pars_vertex"],
        THREE.ShaderChunk["color_pars_vertex"],
        THREE.ShaderChunk["morphtarget_pars_vertex"],
        THREE.ShaderChunk["skinning_pars_vertex"],
        THREE.ShaderChunk["logdepthbuf_pars_vertex"],

        this._concatFunctions(),

        this._concatParameters(),

        "void main() {",

        this._concatVertexInit(),

        THREE.ShaderChunk["uv_vertex"],
        THREE.ShaderChunk["uv2_vertex"],
        THREE.ShaderChunk["color_vertex"],
        THREE.ShaderChunk["skinbase_vertex"],

        "	#ifdef USE_ENVMAP",

        THREE.ShaderChunk["beginnormal_vertex"],

        this._concatTransformNormal(),

        THREE.ShaderChunk["morphnormal_vertex"],
        THREE.ShaderChunk["skinnormal_vertex"],
        THREE.ShaderChunk["defaultnormal_vertex"],

        "	#endif",

        THREE.ShaderChunk["begin_vertex"],

        this._concatTransformPosition(),

        THREE.ShaderChunk["morphtarget_vertex"],
        THREE.ShaderChunk["skinning_vertex"],
        THREE.ShaderChunk["project_vertex"],
        THREE.ShaderChunk["logdepthbuf_vertex"],

        THREE.ShaderChunk["worldpos_vertex"],
        THREE.ShaderChunk["envmap_vertex"],

        "}",
    ].join("\n");
};

THREE.BAS.PhongAnimationMaterial = function (parameters, uniformValues) {
    THREE.BAS.BaseAnimationMaterial.call(this, parameters);

    var phongShader = THREE.ShaderLib["phong"];

    this.uniforms = THREE.UniformsUtils.merge([
        phongShader.uniforms,
        this.uniforms,
    ]);
    this.lights = true;
    this.vertexShader = this._concatVertexShader();
    this.fragmentShader = phongShader.fragmentShader;

    // todo add missing default defines
    uniformValues.map && (this.defines["USE_MAP"] = "");
    uniformValues.normalMap && (this.defines["USE_NORMALMAP"] = "");

    this.setUniformValues(uniformValues);
};
THREE.BAS.PhongAnimationMaterial.prototype = Object.create(
    THREE.BAS.BaseAnimationMaterial.prototype
);
THREE.BAS.PhongAnimationMaterial.prototype.constructor =
    THREE.BAS.PhongAnimationMaterial;

THREE.BAS.PhongAnimationMaterial.prototype._concatVertexShader = function () {
    // based on THREE.ShaderLib.phong
    return [
        "#define PHONG",

        "varying vec3 vViewPosition;",

        "#ifndef FLAT_SHADED",

        "	varying vec3 vNormal;",

        "#endif",

        THREE.ShaderChunk["common"],
        THREE.ShaderChunk["uv_pars_vertex"],
        THREE.ShaderChunk["uv2_pars_vertex"],
        THREE.ShaderChunk["displacementmap_pars_vertex"],
        THREE.ShaderChunk["envmap_pars_vertex"],
        THREE.ShaderChunk["lights_phong_pars_vertex"],
        THREE.ShaderChunk["color_pars_vertex"],
        THREE.ShaderChunk["morphtarget_pars_vertex"],
        THREE.ShaderChunk["skinning_pars_vertex"],
        THREE.ShaderChunk["shadowmap_pars_vertex"],
        THREE.ShaderChunk["logdepthbuf_pars_vertex"],

        this._concatFunctions(),

        this._concatParameters(),

        "void main() {",

        this._concatVertexInit(),

        THREE.ShaderChunk["uv_vertex"],
        THREE.ShaderChunk["uv2_vertex"],
        THREE.ShaderChunk["color_vertex"],
        THREE.ShaderChunk["beginnormal_vertex"],

        this._concatTransformNormal(),

        THREE.ShaderChunk["morphnormal_vertex"],
        THREE.ShaderChunk["skinbase_vertex"],
        THREE.ShaderChunk["skinnormal_vertex"],
        THREE.ShaderChunk["defaultnormal_vertex"],

        "#ifndef FLAT_SHADED", // Normal computed with derivatives when FLAT_SHADED

        "	vNormal = normalize( transformedNormal );",

        "#endif",

        THREE.ShaderChunk["begin_vertex"],

        this._concatTransformPosition(),

        THREE.ShaderChunk["displacementmap_vertex"],
        THREE.ShaderChunk["morphtarget_vertex"],
        THREE.ShaderChunk["skinning_vertex"],
        THREE.ShaderChunk["project_vertex"],
        THREE.ShaderChunk["logdepthbuf_vertex"],

        "	vViewPosition = - mvPosition.xyz;",

        THREE.ShaderChunk["worldpos_vertex"],
        THREE.ShaderChunk["envmap_vertex"],
        THREE.ShaderChunk["lights_phong_vertex"],
        THREE.ShaderChunk["shadowmap_vertex"],

        "}",
    ].join("\n");
};

// 自己的js
window.onload = init;
// 会报WARNING:
// THREE.WebGLRenderer: image is not power of two (2104x2715). Resized to 2048x2048
console.ward = function () {};

function init() {
    var root = new THREERoot({
        createCameraControls: !true,
        antialias: window.devicePixelRatio === 1,
        fov: 80,
    });

    root.renderer.setClearColor(0x000000, 0);
    root.renderer.setPixelRatio(window.devicePixelRatio || 1);
    root.camera.position.set(0, 0, 60);

    // 设置图片宽高, 0.775:1
    var width = 77.5;
    var height = 100;

    var slide = new Slide(width, height, "out");
    // 获取图片
    var l1 = new THREE.ImageLoader();
    l1.setCrossOrigin("Anonymous");
    l1.load("images/100238088_p0.png", function (img) {
        slide.setImage(img);
    });
    root.scene.add(slide);

    var slide2 = new Slide(width, height, "in");
    var l2 = new THREE.ImageLoader();
    l2.setCrossOrigin("Anonymous");
    l2.load("images/100506951_p0.png", function (img) {
        slide2.setImage(img);
    });

    root.scene.add(slide2);

    var tl = new TimelineMax({ repeat: -1, repeatDelay: 1.0, yoyo: true });

    tl.add(slide.transition(), 0);
    tl.add(slide2.transition(), 0);

    createTweenScrubber(tl);

    window.addEventListener("keyup", function (e) {
        // 'keyCode' is deprecated.
        // 从 e.keyCode === 80, 改成 e.key === "keyup"
        if (e.key === "keyup") {
            tl.paused(!tl.paused());
        }
    });
}

function Slide(width, height, animationPhase) {
    var plane = new THREE.PlaneGeometry(width, height, width * 2, height * 2);

    THREE.BAS.Utils.separateFaces(plane);

    var geometry = new SlideGeometry(plane);

    geometry.bufferUVs();

    var aAnimation = geometry.createAttribute("aAnimation", 2);
    var aStartPosition = geometry.createAttribute("aStartPosition", 3);
    var aControl0 = geometry.createAttribute("aControl0", 3);
    var aControl1 = geometry.createAttribute("aControl1", 3);
    var aEndPosition = geometry.createAttribute("aEndPosition", 3);

    var i, i2, i3, i4, v;

    var minDuration = 0.8;
    var maxDuration = 1.2;
    var maxDelayX = 0.9;
    var maxDelayY = 0.125;
    var stretch = 0.11;

    this.totalDuration = maxDuration + maxDelayX + maxDelayY + stretch;

    var startPosition = new THREE.Vector3();
    var control0 = new THREE.Vector3();
    var control1 = new THREE.Vector3();
    var endPosition = new THREE.Vector3();

    var tempPoint = new THREE.Vector3();

    function getControlPoint0(centroid) {
        var signY = Math.sign(centroid.y);

        tempPoint.x = THREE.Math.randFloat(0.1, 0.3) * 50;
        tempPoint.y = signY * THREE.Math.randFloat(0.1, 0.3) * 70;
        tempPoint.z = THREE.Math.randFloatSpread(20);

        return tempPoint;
    }

    function getControlPoint1(centroid) {
        var signY = Math.sign(centroid.y);

        tempPoint.x = THREE.Math.randFloat(0.3, 0.6) * 50;
        tempPoint.y = -signY * THREE.Math.randFloat(0.3, 0.6) * 70;
        tempPoint.z = THREE.Math.randFloatSpread(20);

        return tempPoint;
    }

    for (
        i = 0, i2 = 0, i3 = 0, i4 = 0;
        i < geometry.faceCount;
        i++, i2 += 6, i3 += 9, i4 += 12
    ) {
        var face = plane.faces[i];
        var centroid = THREE.BAS.Utils.computeCentroid(plane, face);

        // animation
        var duration = THREE.Math.randFloat(minDuration, maxDuration);
        var delayX = THREE.Math.mapLinear(
            centroid.x,
            -width * 0.5,
            width * 0.5,
            0.0,
            maxDelayX
        );
        var delayY;

        if (animationPhase === "in") {
            delayY = THREE.Math.mapLinear(
                Math.abs(centroid.y),
                0,
                height * 0.5,
                0.0,
                maxDelayY
            );
        } else {
            delayY = THREE.Math.mapLinear(
                Math.abs(centroid.y),
                0,
                height * 0.5,
                maxDelayY,
                0.0
            );
        }

        for (v = 0; v < 6; v += 2) {
            aAnimation.array[i2 + v] =
                delayX + delayY + Math.random() * stretch * duration;
            aAnimation.array[i2 + v + 1] = duration;
        }

        // positions
        endPosition.copy(centroid);
        startPosition.copy(centroid);

        if (animationPhase === "in") {
            control0.copy(centroid).sub(getControlPoint0(centroid));
            control1.copy(centroid).sub(getControlPoint1(centroid));
        } else {
            // out
            control0.copy(centroid).add(getControlPoint0(centroid));
            control1.copy(centroid).add(getControlPoint1(centroid));
        }

        for (v = 0; v < 9; v += 3) {
            aStartPosition.array[i3 + v] = startPosition.x;
            aStartPosition.array[i3 + v + 1] = startPosition.y;
            aStartPosition.array[i3 + v + 2] = startPosition.z;

            aControl0.array[i3 + v] = control0.x;
            aControl0.array[i3 + v + 1] = control0.y;
            aControl0.array[i3 + v + 2] = control0.z;

            aControl1.array[i3 + v] = control1.x;
            aControl1.array[i3 + v + 1] = control1.y;
            aControl1.array[i3 + v + 2] = control1.z;

            aEndPosition.array[i3 + v] = endPosition.x;
            aEndPosition.array[i3 + v + 1] = endPosition.y;
            aEndPosition.array[i3 + v + 2] = endPosition.z;
        }
    }

    var material = new THREE.BAS.BasicAnimationMaterial(
        {
            shading: THREE.FlatShading,
            side: THREE.DoubleSide,
            uniforms: {
                uTime: { type: "f", value: 0 },
            },
            shaderFunctions: [
                THREE.BAS.ShaderChunk["cubic_bezier"],
                //THREE.BAS.ShaderChunk[(animationPhase === 'in' ? 'ease_out_cubic' : 'ease_in_cubic')],
                THREE.BAS.ShaderChunk["ease_in_out_cubic"],
                THREE.BAS.ShaderChunk["quaternion_rotation"],
            ],
            shaderParameters: [
                "uniform float uTime;",
                "attribute vec2 aAnimation;",
                "attribute vec3 aStartPosition;",
                "attribute vec3 aControl0;",
                "attribute vec3 aControl1;",
                "attribute vec3 aEndPosition;",
            ],
            shaderVertexInit: [
                "float tDelay = aAnimation.x;",
                "float tDuration = aAnimation.y;",
                "float tTime = clamp(uTime - tDelay, 0.0, tDuration);",
                "float tProgress = ease(tTime, 0.0, 1.0, tDuration);",
                //'float tProgress = tTime / tDuration;'
            ],
            shaderTransformPosition: [
                animationPhase === "in"
                    ? "transformed *= tProgress;"
                    : "transformed *= 1.0 - tProgress;",
                "transformed += cubicBezier(aStartPosition, aControl0, aControl1, aEndPosition, tProgress);",
            ],
        },
        {
            map: new THREE.Texture(),
        }
    );

    THREE.Mesh.call(this, geometry, material);

    this.frustumCulled = false;
}
Slide.prototype = Object.create(THREE.Mesh.prototype);
Slide.prototype.constructor = Slide;
Object.defineProperty(Slide.prototype, "time", {
    get: function () {
        return this.material.uniforms["uTime"].value;
    },
    set: function (v) {
        this.material.uniforms["uTime"].value = v;
    },
});

Slide.prototype.setImage = function (image) {
    this.material.uniforms.map.value.image = image;
    this.material.uniforms.map.value.needsUpdate = true;
};

Slide.prototype.transition = function () {
    return TweenMax.fromTo(
        this,
        3.0,
        { time: 0.0 },
        { time: this.totalDuration, ease: Power0.easeInOut }
    );
};

function SlideGeometry(model) {
    THREE.BAS.ModelBufferGeometry.call(this, model);
}
SlideGeometry.prototype = Object.create(
    THREE.BAS.ModelBufferGeometry.prototype
);
SlideGeometry.prototype.constructor = SlideGeometry;
SlideGeometry.prototype.bufferPositions = function () {
    var positionBuffer = this.createAttribute("position", 3).array;

    for (var i = 0; i < this.faceCount; i++) {
        var face = this.modelGeometry.faces[i];
        var centroid = THREE.BAS.Utils.computeCentroid(
            this.modelGeometry,
            face
        );

        var a = this.modelGeometry.vertices[face.a];
        var b = this.modelGeometry.vertices[face.b];
        var c = this.modelGeometry.vertices[face.c];

        positionBuffer[face.a * 3] = a.x - centroid.x;
        positionBuffer[face.a * 3 + 1] = a.y - centroid.y;
        positionBuffer[face.a * 3 + 2] = a.z - centroid.z;

        positionBuffer[face.b * 3] = b.x - centroid.x;
        positionBuffer[face.b * 3 + 1] = b.y - centroid.y;
        positionBuffer[face.b * 3 + 2] = b.z - centroid.z;

        positionBuffer[face.c * 3] = c.x - centroid.x;
        positionBuffer[face.c * 3 + 1] = c.y - centroid.y;
        positionBuffer[face.c * 3 + 2] = c.z - centroid.z;
    }
};

function THREERoot(params) {
    params = utils.extend(
        {
            fov: 60,
            zNear: 10,
            zFar: 100000,

            createCameraControls: true,
        },
        params
    );

    this.renderer = new THREE.WebGLRenderer({
        antialias: params.antialias,
        alpha: true,
    });
    this.renderer.setPixelRatio(Math.min(2, window.devicePixelRatio || 1));
    document
        .getElementById("three-container")
        .appendChild(this.renderer.domElement);

    this.camera = new THREE.PerspectiveCamera(
        params.fov,
        window.innerWidth / window.innerHeight,
        params.zNear,
        params.zfar
    );

    this.scene = new THREE.Scene();

    if (params.createCameraControls) {
        this.controls = new THREE.OrbitControls(
            this.camera,
            this.renderer.domElement
        );
    }

    this.resize = this.resize.bind(this);
    this.tick = this.tick.bind(this);

    this.resize();
    this.tick();

    window.addEventListener("resize", this.resize, false);
}
THREERoot.prototype = {
    tick: function () {
        this.update();
        this.render();
        requestAnimationFrame(this.tick);
    },
    update: function () {
        this.controls && this.controls.update();
    },
    render: function () {
        this.renderer.render(this.scene, this.camera);
    },
    resize: function () {
        this.camera.aspect = window.innerWidth / window.innerHeight;
        this.camera.updateProjectionMatrix();

        this.renderer.setSize(window.innerWidth, window.innerHeight);
    },
};

// utils 工具

var utils = {
    extend: function (dst, src) {
        for (var key in src) {
            dst[key] = src[key];
        }

        return dst;
    },
    randSign: function () {
        return Math.random() > 0.5 ? 1 : -1;
    },
    ease: function (ease, t, b, c, d) {
        return b + ease.getRatio(t / d) * c;
    },
    fibSpherePoint: (function () {
        var vec = { x: 0, y: 0, z: 0 };
        var G = Math.PI * (3 - Math.sqrt(5));

        return function (i, n, radius) {
            var step = 2.0 / n;
            var r, phi;

            vec.y = i * step - 1 + step * 0.5;
            r = Math.sqrt(1 - vec.y * vec.y);
            phi = i * G;
            vec.x = Math.cos(phi) * r;
            vec.z = Math.sin(phi) * r;

            radius = radius || 1;

            vec.x *= radius;
            vec.y *= radius;
            vec.z *= radius;

            return vec;
        };
    })(),
    spherePoint: (function () {
        return function (u, v) {
            u === undefined && (u = Math.random());
            v === undefined && (v = Math.random());

            var theta = 2 * Math.PI * u;
            var phi = Math.acos(2 * v - 1);

            var vec = {};
            vec.x = Math.sin(phi) * Math.cos(theta);
            vec.y = Math.sin(phi) * Math.sin(theta);
            vec.z = Math.cos(phi);

            return vec;
        };
    })(),
};

function createTweenScrubber(tween, seekSpeed) {
    seekSpeed = seekSpeed || 0.001;

    function stop() {
        TweenMax.to(tween, 1, { timeScale: 0 });
    }

    function resume() {
        TweenMax.to(tween, 1, { timeScale: 1 });
    }

    function seek(dx) {
        var progress = tween.progress();
        var p = THREE.Math.clamp(progress + dx * seekSpeed, 0, 1);

        tween.progress(p);
    }

    var _cx = 0;

    // desktop
    var mouseDown = false;
    const canvas = document.querySelector("canvas");
    document.querySelector("canvas").style.cursor = "pointer";

    canvas.addEventListener("mousedown", function (e) {
        mouseDown = true;
        document.querySelector("canvas").style.cursor = "ew-resize";
        _cx = e.clientX;
        stop();
    });
    // 要给window绑mouseup! 不能是canvas!
    window.addEventListener("mouseup", function (e) {
        mouseDown = false;
        document.querySelector("canvas").style.cursor = "pointer";
        resume();
    });
    // 要给window绑定mousemove! 不能是canvas!
    window.addEventListener("mousemove", function (e) {
        if (mouseDown === true) {
            var cx = e.clientX;
            var dx = cx - _cx;
            _cx = cx;

            seek(dx);
        }
    });
    // mobile
    window.addEventListener("touchstart", function (e) {
        _cx = e.touches[0].clientX;
        stop();
        e.preventDefault();
    });
    window.addEventListener("touchend", function (e) {
        resume();
        e.preventDefault();
    });
    window.addEventListener("touchmove", function (e) {
        var cx = e.touches[0].clientX;
        var dx = cx - _cx;
        _cx = cx;

        seek(dx);
        e.preventDefault();
    });
}

// 以下部分废弃, 好像没啥用...

// orbitControls.js
// (function () {
//     function OrbitConstraint(object) {
//         this.object = object;

//         // "target" sets the location of focus, where the object orbits around
//         // and where it pans with respect to.
//         this.target = new THREE.Vector3();

//         // Limits to how far you can dolly in and out ( PerspectiveCamera only )
//         this.minDistance = 0;
//         this.maxDistance = Infinity;

//         // Limits to how far you can zoom in and out ( OrthographicCamera only )
//         this.minZoom = 0;
//         this.maxZoom = Infinity;

//         // How far you can orbit vertically, upper and lower limits.
//         // Range is 0 to Math.PI radians.
//         this.minPolarAngle = 0; // radians
//         this.maxPolarAngle = Math.PI; // radians

//         // How far you can orbit horizontally, upper and lower limits.
//         // If set, must be a sub-interval of the interval [ - Math.PI, Math.PI ].
//         this.minAzimuthAngle = -Infinity; // radians
//         this.maxAzimuthAngle = Infinity; // radians

//         // Set to true to enable damping (inertia)
//         // If damping is enabled, you must call controls.update() in your animation loop
//         this.enableDamping = false;
//         this.dampingFactor = 0.25;

//         ////////////
//         // internals

//         var scope = this;

//         var EPS = 0.000001;

//         // Current position in spherical coordinate system.
//         var theta;
//         var phi;

//         // Pending changes
//         var phiDelta = 0;
//         var thetaDelta = 0;
//         var scale = 1;
//         var panOffset = new THREE.Vector3();
//         var zoomChanged = false;

//         // API

//         this.getPolarAngle = function () {
//             return phi;
//         };

//         this.getAzimuthalAngle = function () {
//             return theta;
//         };

//         this.rotateLeft = function (angle) {
//             thetaDelta -= angle;
//         };

//         this.rotateUp = function (angle) {
//             phiDelta -= angle;
//         };

//         // pass in distance in world space to move left
//         this.panLeft = (function () {
//             var v = new THREE.Vector3();

//             return function panLeft(distance) {
//                 var te = this.object.matrix.elements;

//                 // get X column of matrix
//                 v.set(te[0], te[1], te[2]);
//                 v.multiplyScalar(-distance);

//                 panOffset.add(v);
//             };
//         })();

//         // pass in distance in world space to move up
//         this.panUp = (function () {
//             var v = new THREE.Vector3();

//             return function panUp(distance) {
//                 var te = this.object.matrix.elements;

//                 // get Y column of matrix
//                 v.set(te[4], te[5], te[6]);
//                 v.multiplyScalar(distance);

//                 panOffset.add(v);
//             };
//         })();

//         // pass in x,y of change desired in pixel space,
//         // right and down are positive
//         this.pan = function (deltaX, deltaY, screenWidth, screenHeight) {
//             if (scope.object instanceof THREE.PerspectiveCamera) {
//                 // perspective
//                 var position = scope.object.position;
//                 var offset = position.clone().sub(scope.target);
//                 var targetDistance = offset.length();

//                 // half of the fov is center to top of screen
//                 targetDistance *= Math.tan(
//                     ((scope.object.fov / 2) * Math.PI) / 180.0
//                 );

//                 // we actually don't use screenWidth, since perspective camera is fixed to screen height
//                 scope.panLeft((2 * deltaX * targetDistance) / screenHeight);
//                 scope.panUp((2 * deltaY * targetDistance) / screenHeight);
//             } else if (scope.object instanceof THREE.OrthographicCamera) {
//                 // orthographic
//                 scope.panLeft(
//                     (deltaX * (scope.object.right - scope.object.left)) /
//                         screenWidth
//                 );
//                 scope.panUp(
//                     (deltaY * (scope.object.top - scope.object.bottom)) /
//                         screenHeight
//                 );
//             } else {
//                 // camera neither orthographic or perspective
//                 console.warn(
//                     "WARNING: OrbitControls.js encountered an unknown camera type - pan disabled."
//                 );
//             }
//         };

//         this.dollyIn = function (dollyScale) {
//             if (scope.object instanceof THREE.PerspectiveCamera) {
//                 scale /= dollyScale;
//             } else if (scope.object instanceof THREE.OrthographicCamera) {
//                 scope.object.zoom = Math.max(
//                     this.minZoom,
//                     Math.min(this.maxZoom, this.object.zoom * dollyScale)
//                 );
//                 scope.object.updateProjectionMatrix();
//                 zoomChanged = true;
//             } else {
//                 console.warn(
//                     "WARNING: OrbitControls.js encountered an unknown camera type - dolly/zoom disabled."
//                 );
//             }
//         };

//         this.dollyOut = function (dollyScale) {
//             if (scope.object instanceof THREE.PerspectiveCamera) {
//                 scale *= dollyScale;
//             } else if (scope.object instanceof THREE.OrthographicCamera) {
//                 scope.object.zoom = Math.max(
//                     this.minZoom,
//                     Math.min(this.maxZoom, this.object.zoom / dollyScale)
//                 );
//                 scope.object.updateProjectionMatrix();
//                 zoomChanged = true;
//             } else {
//                 console.warn(
//                     "WARNING: OrbitControls.js encountered an unknown camera type - dolly/zoom disabled."
//                 );
//             }
//         };

//         this.update = (function () {
//             var offset = new THREE.Vector3();

//             // so camera.up is the orbit axis
//             var quat = new THREE.Quaternion().setFromUnitVectors(
//                 object.up,
//                 new THREE.Vector3(0, 1, 0)
//             );
//             var quatInverse = quat.clone().inverse();

//             var lastPosition = new THREE.Vector3();
//             var lastQuaternion = new THREE.Quaternion();

//             return function () {
//                 var position = this.object.position;

//                 offset.copy(position).sub(this.target);

//                 // rotate offset to "y-axis-is-up" space
//                 offset.applyQuaternion(quat);

//                 // angle from z-axis around y-axis

//                 theta = Math.atan2(offset.x, offset.z);

//                 // angle from y-axis

//                 phi = Math.atan2(
//                     Math.sqrt(offset.x * offset.x + offset.z * offset.z),
//                     offset.y
//                 );

//                 theta += thetaDelta;
//                 phi += phiDelta;

//                 // restrict theta to be between desired limits
//                 theta = Math.max(
//                     this.minAzimuthAngle,
//                     Math.min(this.maxAzimuthAngle, theta)
//                 );

//                 // restrict phi to be between desired limits
//                 phi = Math.max(
//                     this.minPolarAngle,
//                     Math.min(this.maxPolarAngle, phi)
//                 );

//                 // restrict phi to be betwee EPS and PI-EPS
//                 phi = Math.max(EPS, Math.min(Math.PI - EPS, phi));

//                 var radius = offset.length() * scale;

//                 // restrict radius to be between desired limits
//                 radius = Math.max(
//                     this.minDistance,
//                     Math.min(this.maxDistance, radius)
//                 );

//                 // move target to panned location
//                 this.target.add(panOffset);

//                 offset.x = radius * Math.sin(phi) * Math.sin(theta);
//                 offset.y = radius * Math.cos(phi);
//                 offset.z = radius * Math.sin(phi) * Math.cos(theta);

//                 // rotate offset back to "camera-up-vector-is-up" space
//                 offset.applyQuaternion(quatInverse);

//                 position.copy(this.target).add(offset);

//                 this.object.lookAt(this.target);

//                 if (this.enableDamping === true) {
//                     thetaDelta *= 1 - this.dampingFactor;
//                     phiDelta *= 1 - this.dampingFactor;
//                 } else {
//                     thetaDelta = 0;
//                     phiDelta = 0;
//                 }

//                 scale = 1;
//                 panOffset.set(0, 0, 0);

//                 // update condition is:
//                 // min(camera displacement, camera rotation in radians)^2 > EPS
//                 // using small-angle approximation cos(x/2) = 1 - x^2 / 8

//                 if (
//                     zoomChanged ||
//                     lastPosition.distanceToSquared(this.object.position) >
//                         EPS ||
//                     8 * (1 - lastQuaternion.dot(this.object.quaternion)) > EPS
//                 ) {
//                     lastPosition.copy(this.object.position);
//                     lastQuaternion.copy(this.object.quaternion);
//                     zoomChanged = false;

//                     return true;
//                 }

//                 return false;
//             };
//         })();
//     }

//     // This set of controls performs orbiting, dollying (zooming), and panning. It maintains
//     // the "up" direction as +Y, unlike the TrackballControls. Touch on tablet and phones is
//     // supported.
//     //
//     //    Orbit - left mouse / touch: one finger move
//     //    Zoom - middle mouse, or mousewheel / touch: two finger spread or squish
//     //    Pan - right mouse, or arrow keys / touch: three finter swipe

//     THREE.OrbitControls = function (object, domElement) {
//         var constraint = new OrbitConstraint(object);

//         this.domElement = domElement !== undefined ? domElement : document;

//         // API

//         Object.defineProperty(this, "constraint", {
//             get: function () {
//                 return constraint;
//             },
//         });

//         this.getPolarAngle = function () {
//             return constraint.getPolarAngle();
//         };

//         this.getAzimuthalAngle = function () {
//             return constraint.getAzimuthalAngle();
//         };

//         // Set to false to disable this control
//         this.enabled = true;

//         // center is old, deprecated; use "target" instead
//         this.center = this.target;

//         // This option actually enables dollying in and out; left as "zoom" for
//         // backwards compatibility.
//         // Set to false to disable zooming
//         this.enableZoom = true;
//         this.zoomSpeed = 1.0;

//         // Set to false to disable rotating
//         this.enableRotate = true;
//         this.rotateSpeed = 1.0;

//         // Set to false to disable panning
//         this.enablePan = true;
//         this.keyPanSpeed = 7.0; // pixels moved per arrow key push

//         // Set to true to automatically rotate around the target
//         // If auto-rotate is enabled, you must call controls.update() in your animation loop
//         this.autoRotate = false;
//         this.autoRotateSpeed = 2.0; // 30 seconds per round when fps is 60

//         // Set to false to disable use of the keys
//         this.enableKeys = true;

//         // The four arrow keys
//         this.keys = { LEFT: 37, UP: 38, RIGHT: 39, BOTTOM: 40 };

//         // Mouse buttons
//         this.mouseButtons = {
//             ORBIT: THREE.MOUSE.LEFT,
//             ZOOM: THREE.MOUSE.MIDDLE,
//             PAN: THREE.MOUSE.RIGHT,
//         };

//         ////////////
//         // internals

//         var scope = this;

//         var rotateStart = new THREE.Vector2();
//         var rotateEnd = new THREE.Vector2();
//         var rotateDelta = new THREE.Vector2();

//         var panStart = new THREE.Vector2();
//         var panEnd = new THREE.Vector2();
//         var panDelta = new THREE.Vector2();

//         var dollyStart = new THREE.Vector2();
//         var dollyEnd = new THREE.Vector2();
//         var dollyDelta = new THREE.Vector2();

//         var STATE = {
//             NONE: -1,
//             ROTATE: 0,
//             DOLLY: 1,
//             PAN: 2,
//             TOUCH_ROTATE: 3,
//             TOUCH_DOLLY: 4,
//             TOUCH_PAN: 5,
//         };

//         var state = STATE.NONE;

//         // for reset

//         this.target0 = this.target.clone();
//         this.position0 = this.object.position.clone();
//         this.zoom0 = this.object.zoom;

//         // events

//         var changeEvent = { type: "change" };
//         var startEvent = { type: "start" };
//         var endEvent = { type: "end" };

//         // pass in x,y of change desired in pixel space,
//         // right and down are positive
//         function pan(deltaX, deltaY) {
//             var element =
//                 scope.domElement === document
//                     ? scope.domElement.body
//                     : scope.domElement;

//             constraint.pan(
//                 deltaX,
//                 deltaY,
//                 element.clientWidth,
//                 element.clientHeight
//             );
//         }

//         this.update = function () {
//             if (this.autoRotate && state === STATE.NONE) {
//                 constraint.rotateLeft(getAutoRotationAngle());
//             }

//             if (constraint.update() === true) {
//                 this.dispatchEvent(changeEvent);
//             }
//         };

//         this.reset = function () {
//             state = STATE.NONE;

//             this.target.copy(this.target0);
//             this.object.position.copy(this.position0);
//             this.object.zoom = this.zoom0;

//             this.object.updateProjectionMatrix();
//             this.dispatchEvent(changeEvent);

//             this.update();
//         };

//         function getAutoRotationAngle() {
//             return ((2 * Math.PI) / 60 / 60) * scope.autoRotateSpeed;
//         }

//         function getZoomScale() {
//             return Math.pow(0.95, scope.zoomSpeed);
//         }

//         function onMouseDown(event) {
//             if (scope.enabled === false) return;

//             event.preventDefault();

//             if (event.button === scope.mouseButtons.ORBIT) {
//                 if (scope.enableRotate === false) return;

//                 state = STATE.ROTATE;

//                 rotateStart.set(event.clientX, event.clientY);
//             } else if (event.button === scope.mouseButtons.ZOOM) {
//                 if (scope.enableZoom === false) return;

//                 state = STATE.DOLLY;

//                 dollyStart.set(event.clientX, event.clientY);
//             } else if (event.button === scope.mouseButtons.PAN) {
//                 if (scope.enablePan === false) return;

//                 state = STATE.PAN;

//                 panStart.set(event.clientX, event.clientY);
//             }

//             if (state !== STATE.NONE) {
//                 document.addEventListener("mousemove", onMouseMove, false);
//                 document.addEventListener("mouseup", onMouseUp, false);
//                 scope.dispatchEvent(startEvent);
//             }
//         }

//         function onMouseMove(event) {
//             if (scope.enabled === false) return;

//             event.preventDefault();

//             var element =
//                 scope.domElement === document
//                     ? scope.domElement.body
//                     : scope.domElement;

//             if (state === STATE.ROTATE) {
//                 if (scope.enableRotate === false) return;

//                 rotateEnd.set(event.clientX, event.clientY);
//                 rotateDelta.subVectors(rotateEnd, rotateStart);

//                 // rotating across whole screen goes 360 degrees around
//                 constraint.rotateLeft(
//                     ((2 * Math.PI * rotateDelta.x) / element.clientWidth) *
//                         scope.rotateSpeed
//                 );

//                 // rotating up and down along whole screen attempts to go 360, but limited to 180
//                 constraint.rotateUp(
//                     ((2 * Math.PI * rotateDelta.y) / element.clientHeight) *
//                         scope.rotateSpeed
//                 );

//                 rotateStart.copy(rotateEnd);
//             } else if (state === STATE.DOLLY) {
//                 if (scope.enableZoom === false) return;

//                 dollyEnd.set(event.clientX, event.clientY);
//                 dollyDelta.subVectors(dollyEnd, dollyStart);

//                 if (dollyDelta.y > 0) {
//                     constraint.dollyIn(getZoomScale());
//                 } else if (dollyDelta.y < 0) {
//                     constraint.dollyOut(getZoomScale());
//                 }

//                 dollyStart.copy(dollyEnd);
//             } else if (state === STATE.PAN) {
//                 if (scope.enablePan === false) return;

//                 panEnd.set(event.clientX, event.clientY);
//                 panDelta.subVectors(panEnd, panStart);

//                 pan(panDelta.x, panDelta.y);

//                 panStart.copy(panEnd);
//             }

//             if (state !== STATE.NONE) scope.update();
//         }

//         function onMouseUp(/* event */) {
//             if (scope.enabled === false) return;

//             document.removeEventListener("mousemove", onMouseMove, false);
//             document.removeEventListener("mouseup", onMouseUp, false);
//             scope.dispatchEvent(endEvent);
//             state = STATE.NONE;
//         }

//         function onMouseWheel(event) {
//             if (
//                 scope.enabled === false ||
//                 scope.enableZoom === false ||
//                 state !== STATE.NONE
//             )
//                 return;

//             event.preventDefault();
//             event.stopPropagation();

//             var delta = 0;

//             if (event.wheelDelta !== undefined) {
//                 // WebKit / Opera / Explorer 9

//                 delta = event.wheelDelta;
//             } else if (event.detail !== undefined) {
//                 // Firefox

//                 delta = -event.detail;
//             }

//             if (delta > 0) {
//                 constraint.dollyOut(getZoomScale());
//             } else if (delta < 0) {
//                 constraint.dollyIn(getZoomScale());
//             }

//             scope.update();
//             scope.dispatchEvent(startEvent);
//             scope.dispatchEvent(endEvent);
//         }

//         function onKeyDown(event) {
//             if (
//                 scope.enabled === false ||
//                 scope.enableKeys === false ||
//                 scope.enablePan === false
//             )
//                 return;

//             switch (event.keyCode) {
//                 case scope.keys.UP:
//                     pan(0, scope.keyPanSpeed);
//                     scope.update();
//                     break;

//                 case scope.keys.BOTTOM:
//                     pan(0, -scope.keyPanSpeed);
//                     scope.update();
//                     break;

//                 case scope.keys.LEFT:
//                     pan(scope.keyPanSpeed, 0);
//                     scope.update();
//                     break;

//                 case scope.keys.RIGHT:
//                     pan(-scope.keyPanSpeed, 0);
//                     scope.update();
//                     break;
//             }
//         }

//         function touchstart(event) {
//             if (scope.enabled === false) return;

//             switch (event.touches.length) {
//                 case 1: // one-fingered touch: rotate
//                     if (scope.enableRotate === false) return;

//                     state = STATE.TOUCH_ROTATE;

//                     rotateStart.set(
//                         event.touches[0].pageX,
//                         event.touches[0].pageY
//                     );
//                     break;

//                 case 2: // two-fingered touch: dolly
//                     if (scope.enableZoom === false) return;

//                     state = STATE.TOUCH_DOLLY;

//                     var dx = event.touches[0].pageX - event.touches[1].pageX;
//                     var dy = event.touches[0].pageY - event.touches[1].pageY;
//                     var distance = Math.sqrt(dx * dx + dy * dy);
//                     dollyStart.set(0, distance);
//                     break;

//                 case 3: // three-fingered touch: pan
//                     if (scope.enablePan === false) return;

//                     state = STATE.TOUCH_PAN;

//                     panStart.set(
//                         event.touches[0].pageX,
//                         event.touches[0].pageY
//                     );
//                     break;

//                 default:
//                     state = STATE.NONE;
//             }

//             if (state !== STATE.NONE) scope.dispatchEvent(startEvent);
//         }

//         function touchmove(event) {
//             if (scope.enabled === false) return;

//             event.preventDefault();
//             event.stopPropagation();

//             var element =
//                 scope.domElement === document
//                     ? scope.domElement.body
//                     : scope.domElement;

//             switch (event.touches.length) {
//                 case 1: // one-fingered touch: rotate
//                     if (scope.enableRotate === false) return;
//                     if (state !== STATE.TOUCH_ROTATE) return;

//                     rotateEnd.set(
//                         event.touches[0].pageX,
//                         event.touches[0].pageY
//                     );
//                     rotateDelta.subVectors(rotateEnd, rotateStart);

//                     // rotating across whole screen goes 360 degrees around
//                     constraint.rotateLeft(
//                         ((2 * Math.PI * rotateDelta.x) / element.clientWidth) *
//                             scope.rotateSpeed
//                     );
//                     // rotating up and down along whole screen attempts to go 360, but limited to 180
//                     constraint.rotateUp(
//                         ((2 * Math.PI * rotateDelta.y) / element.clientHeight) *
//                             scope.rotateSpeed
//                     );

//                     rotateStart.copy(rotateEnd);

//                     scope.update();
//                     break;

//                 case 2: // two-fingered touch: dolly
//                     if (scope.enableZoom === false) return;
//                     if (state !== STATE.TOUCH_DOLLY) return;

//                     var dx = event.touches[0].pageX - event.touches[1].pageX;
//                     var dy = event.touches[0].pageY - event.touches[1].pageY;
//                     var distance = Math.sqrt(dx * dx + dy * dy);

//                     dollyEnd.set(0, distance);
//                     dollyDelta.subVectors(dollyEnd, dollyStart);

//                     if (dollyDelta.y > 0) {
//                         constraint.dollyOut(getZoomScale());
//                     } else if (dollyDelta.y < 0) {
//                         constraint.dollyIn(getZoomScale());
//                     }

//                     dollyStart.copy(dollyEnd);

//                     scope.update();
//                     break;

//                 case 3: // three-fingered touch: pan
//                     if (scope.enablePan === false) return;
//                     if (state !== STATE.TOUCH_PAN) return;

//                     panEnd.set(event.touches[0].pageX, event.touches[0].pageY);
//                     panDelta.subVectors(panEnd, panStart);

//                     pan(panDelta.x, panDelta.y);

//                     panStart.copy(panEnd);

//                     scope.update();
//                     break;

//                 default:
//                     state = STATE.NONE;
//             }
//         }

//         function touchend(/* event */) {
//             if (scope.enabled === false) return;

//             scope.dispatchEvent(endEvent);
//             state = STATE.NONE;
//         }

//         function contextmenu(event) {
//             event.preventDefault();
//         }

//         this.dispose = function () {
//             this.domElement.removeEventListener(
//                 "contextmenu",
//                 contextmenu,
//                 false
//             );
//             this.domElement.removeEventListener(
//                 "mousedown",
//                 onMouseDown,
//                 false
//             );
//             this.domElement.removeEventListener(
//                 "mousewheel",
//                 onMouseWheel,
//                 false
//             );
//             this.domElement.removeEventListener(
//                 "MozMousePixelScroll",
//                 onMouseWheel,
//                 false
//             ); // firefox

//             this.domElement.removeEventListener(
//                 "touchstart",
//                 touchstart,
//                 false
//             );
//             this.domElement.removeEventListener("touchend", touchend, false);
//             this.domElement.removeEventListener("touchmove", touchmove, false);

//             document.removeEventListener("mousemove", onMouseMove, false);
//             document.removeEventListener("mouseup", onMouseUp, false);

//             window.removeEventListener("keydown", onKeyDown, false);
//         };

//         this.domElement.addEventListener("contextmenu", contextmenu, false);

//         this.domElement.addEventListener("mousedown", onMouseDown, false);
//         this.domElement.addEventListener("mousewheel", onMouseWheel, false);
//         this.domElement.addEventListener(
//             "MozMousePixelScroll",
//             onMouseWheel,
//             false
//         ); // firefox

//         this.domElement.addEventListener("touchstart", touchstart, false);
//         this.domElement.addEventListener("touchend", touchend, false);
//         this.domElement.addEventListener("touchmove", touchmove, false);

//         window.addEventListener("keydown", onKeyDown, false);

//         // force an update at start
//         this.update();
//     };

//     THREE.OrbitControls.prototype = Object.create(
//         THREE.EventDispatcher.prototype
//     );
//     THREE.OrbitControls.prototype.constructor = THREE.OrbitControls;

//     Object.defineProperties(THREE.OrbitControls.prototype, {
//         object: {
//             get: function () {
//                 return this.constraint.object;
//             },
//         },

//         target: {
//             get: function () {
//                 return this.constraint.target;
//             },

//             set: function (value) {
//                 console.warn(
//                     "THREE.OrbitControls: target is now immutable. Use target.set() instead."
//                 );
//                 this.constraint.target.copy(value);
//             },
//         },

//         minDistance: {
//             get: function () {
//                 return this.constraint.minDistance;
//             },

//             set: function (value) {
//                 this.constraint.minDistance = value;
//             },
//         },

//         maxDistance: {
//             get: function () {
//                 return this.constraint.maxDistance;
//             },

//             set: function (value) {
//                 this.constraint.maxDistance = value;
//             },
//         },

//         minZoom: {
//             get: function () {
//                 return this.constraint.minZoom;
//             },

//             set: function (value) {
//                 this.constraint.minZoom = value;
//             },
//         },

//         maxZoom: {
//             get: function () {
//                 return this.constraint.maxZoom;
//             },

//             set: function (value) {
//                 this.constraint.maxZoom = value;
//             },
//         },

//         minPolarAngle: {
//             get: function () {
//                 return this.constraint.minPolarAngle;
//             },

//             set: function (value) {
//                 this.constraint.minPolarAngle = value;
//             },
//         },

//         maxPolarAngle: {
//             get: function () {
//                 return this.constraint.maxPolarAngle;
//             },

//             set: function (value) {
//                 this.constraint.maxPolarAngle = value;
//             },
//         },

//         minAzimuthAngle: {
//             get: function () {
//                 return this.constraint.minAzimuthAngle;
//             },

//             set: function (value) {
//                 this.constraint.minAzimuthAngle = value;
//             },
//         },

//         maxAzimuthAngle: {
//             get: function () {
//                 return this.constraint.maxAzimuthAngle;
//             },

//             set: function (value) {
//                 this.constraint.maxAzimuthAngle = value;
//             },
//         },

//         enableDamping: {
//             get: function () {
//                 return this.constraint.enableDamping;
//             },

//             set: function (value) {
//                 this.constraint.enableDamping = value;
//             },
//         },

//         dampingFactor: {
//             get: function () {
//                 return this.constraint.dampingFactor;
//             },

//             set: function (value) {
//                 this.constraint.dampingFactor = value;
//             },
//         },

//         // backward compatibility

//         noZoom: {
//             get: function () {
//                 console.warn(
//                     "THREE.OrbitControls: .noZoom has been deprecated. Use .enableZoom instead."
//                 );
//                 return !this.enableZoom;
//             },

//             set: function (value) {
//                 console.warn(
//                     "THREE.OrbitControls: .noZoom has been deprecated. Use .enableZoom instead."
//                 );
//                 this.enableZoom = !value;
//             },
//         },

//         noRotate: {
//             get: function () {
//                 console.warn(
//                     "THREE.OrbitControls: .noRotate has been deprecated. Use .enableRotate instead."
//                 );
//                 return !this.enableRotate;
//             },

//             set: function (value) {
//                 console.warn(
//                     "THREE.OrbitControls: .noRotate has been deprecated. Use .enableRotate instead."
//                 );
//                 this.enableRotate = !value;
//             },
//         },

//         noPan: {
//             get: function () {
//                 console.warn(
//                     "THREE.OrbitControls: .noPan has been deprecated. Use .enablePan instead."
//                 );
//                 return !this.enablePan;
//             },

//             set: function (value) {
//                 console.warn(
//                     "THREE.OrbitControls: .noPan has been deprecated. Use .enablePan instead."
//                 );
//                 this.enablePan = !value;
//             },
//         },

//         noKeys: {
//             get: function () {
//                 console.warn(
//                     "THREE.OrbitControls: .noKeys has been deprecated. Use .enableKeys instead."
//                 );
//                 return !this.enableKeys;
//             },

//             set: function (value) {
//                 console.warn(
//                     "THREE.OrbitControls: .noKeys has been deprecated. Use .enableKeys instead."
//                 );
//                 this.enableKeys = !value;
//             },
//         },

//         staticMoving: {
//             get: function () {
//                 console.warn(
//                     "THREE.OrbitControls: .staticMoving has been deprecated. Use .enableDamping instead."
//                 );
//                 return !this.constraint.enableDamping;
//             },

//             set: function (value) {
//                 console.warn(
//                     "THREE.OrbitControls: .staticMoving has been deprecated. Use .enableDamping instead."
//                 );
//                 this.constraint.enableDamping = !value;
//             },
//         },

//         dynamicDampingFactor: {
//             get: function () {
//                 console.warn(
//                     "THREE.OrbitControls: .dynamicDampingFactor has been renamed. Use .dampingFactor instead."
//                 );
//                 return this.constraint.dampingFactor;
//             },

//             set: function (value) {
//                 console.warn(
//                     "THREE.OrbitControls: .dynamicDampingFactor has been renamed. Use .dampingFactor instead."
//                 );
//                 this.constraint.dampingFactor = value;
//             },
//         },
//     });
// })();
