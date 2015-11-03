#include <stdint.h>

typedef int32_t status_t;

// For OMX TF300T /system/lib/libnvwinsys.so
    //SurfaceControl::status_t    setLayer(uint32_t layer);
    extern status_t  _ZN7android14SurfaceControl8setLayerEj(uint32_t layer);

   // status_t    setLayer(int32_t layer);
    status_t _ZN7android14SurfaceControl8setLayerEi(int32_t layer);


status_t _ZN7android14SurfaceControl8setLayerEi(int32_t layer)
{
    return _ZN7android14SurfaceControl8setLayerEj(layer);
}

