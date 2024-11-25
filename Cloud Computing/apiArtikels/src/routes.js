const {
    addArtikelHandler,
    getAllArtikelsHandler,
    getArtikelByIdHandler,
    updateArtikelHandler,
    deleteArtikelHandler,
  } = require('./handler');
  
  const routes = [
    {
      method: 'POST',
      path: '/artikels',
      handler: addArtikelHandler,
    },
    {
      method: 'GET',
      path: '/artikels',
      handler: getAllArtikelsHandler,
    },
    {
      method: 'GET',
      path: '/artikels/{id}',
      handler: getArtikelByIdHandler,
    },
    {
      method: 'PUT',
      path: '/artikels/{id}',
      handler: updateArtikelHandler,
    },
    {
      method: 'DELETE',
      path: '/artikels/{id}',
      handler: deleteArtikelHandler,
    },
  ];
  
  module.exports = routes;
  