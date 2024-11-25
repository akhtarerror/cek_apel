const { v4: uuidv4 } = require('uuid');
const artikels = require('./artikels');

// Handler untuk menambahkan artikel
const addArtikelHandler = (request, h) => {
  const { nama, deskripsi } = request.payload;

  const newArtikel = {
    id: uuidv4(),
    nama,
    deskripsi,
  };

  artikels.push(newArtikel);

  return h.response({
    status: 'success',
    message: 'Artikel berhasil ditambahkan',
    data: newArtikel,
  }).code(201);
};

// Handler untuk mendapatkan semua artikel
const getAllArtikelsHandler = () => ({
  status: 'success',
  data: artikels,
});

// Handler untuk mendapatkan artikel berdasarkan ID
const getArtikelByIdHandler = (request, h) => {
  const { id } = request.params;
  const artikel = artikels.find((a) => a.id === id);

  if (!artikel) {
    return h.response({
      status: 'fail',
      message: 'Artikel tidak ditemukan',
    }).code(404);
  }

  return {
    status: 'success',
    data: artikel,
  };
};

// Handler untuk memperbarui artikel
const updateArtikelHandler = (request, h) => {
  const { id } = request.params;
  const { nama, deskripsi } = request.payload;

  const artikelIndex = artikels.findIndex((a) => a.id === id);

  if (artikelIndex === -1) {
    return h.response({
      status: 'fail',
      message: 'Artikel tidak ditemukan',
    }).code(404);
  }

  artikels[artikelIndex] = {
    ...artikels[artikelIndex],
    nama,
    deskripsi,
  };

  return {
    status: 'success',
    message: 'Artikel berhasil diperbarui',
    data: artikels[artikelIndex],
  };
};

// Handler untuk menghapus artikel
const deleteArtikelHandler = (request, h) => {
  const { id } = request.params;
  const artikelIndex = artikels.findIndex((a) => a.id === id);

  if (artikelIndex === -1) {
    return h.response({
      status: 'fail',
      message: 'Artikel tidak ditemukan',
    }).code(404);
  }

  artikels.splice(artikelIndex, 1);

  return {
    status: 'success',
    message: 'Artikel berhasil dihapus',
  };
};

module.exports = {
  addArtikelHandler,
  getAllArtikelsHandler,
  getArtikelByIdHandler,
  updateArtikelHandler,
  deleteArtikelHandler,
};
