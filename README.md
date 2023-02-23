# Majika: Aplikasi Android Pemesanan Makanan

## Deskripsi Aplikasi
<b> Majika </b> merupakan aplikasi Android untuk melakukan pemesanan makanan sebagai bentuk pemenuhan Tugas Besar 1 IF3210 Pengembangan Aplikasi pada Platform Khusus Tahun 2023. Terdapat lima fitur utama pada aplikasi ini, antara lain Fitur Twibbon, Fitur Cabang Restoran, Fitur Menu, Fitur Keranjang, dan Fitur Pembayaran. Fitur-fitur tersebut adalah bentuk pemenuhan spesifikasi yang dirincikan berikut ini. 
1. <b> Header dan Navbar </b> <br>
Aplikasi ini memiliki header yang menampilkan judul dari fitur yang sedang digunakan dan navbar yang membantu pengguna berpindah dari satu fitur ke fitur lain.
2. <b> Menampilkan Daftar Makanan dan Minuman </b> <br>
Ketika aplikasi diluncurkan, fitur ini yang akan ditampilkan pertama kali. Daftar menu yang ditampilkan pada fitur ini akan dibagi sesuai dengan jenisnya. Menu dapat dicari melalui search bar. Menu dapat ditambahkan secara otomatis ke keranjang. Data menu diambil dari API server Majika menggunakan library Retrofit. Selain menampilkan daftar menu, fitur ini juga menampilkan informasi suhu yang didapatkan dari sensor suhu. 
3. <b> Menampilkan Keranjang </b> <br>
Menu yang berhasil ditambahkan akan ditampilkan pada fitur ini. Akan ditampilkan juga total harga dari menu yang dipilih dan juga tombol bayar untuk mengarah ke pembayaran. Untuk dapat menyimpan dan menampilkan keranjang, daftar keranjang disimpan ke basis data menggunakan Room Repository. 
4. <b> Pembayaran </b> <br>
Pengguna yang sudah memilih menu dapat melakukan pembayaran dengan melakukan scan QR Code. Akan muncul tampilan apakah pembayaran berhasil atau tidak sesuai dengan pembacaan QR Code. Ketika pembayaran berhasil, pengguna akan diarahkan kembali ke fitur menu dan keranjang akan dikosongkan. 
5. <b> Mencari Cabang Restoran </b> <br>
Fitur ini memanfaatkan API Majika untuk menampilkan daftar cabang. Terdapat tombol Maps yang dapat mengarahkan pengguna ke Google Maps sesuai dengan lokasi restoran.
6. <b> Fitur Twibbon </b> <br>
Pengguna dapat menggunakan kameranya dan menangkap foto untuk membuat gambar Twibbon. Foto yang berhasil ditangkap dapat kembali diulang jika pengguna menginginkan. 


## Libray yang Digunakan
1. Retrofit
2. Room
3. ZXing Barcode Scanner


## Screenshot aplikasi
![ss](/Screenshot/splash_menu.png)
![ss](/Screenshot/menu_pot_land.png)
![ss](/Screenshot/keranjang_pembayaran.png)
![ss](/Screenshot/twibbon_cabang.png)


## Pembagian Kerja Anggota Kelompok Beserta Jam Persiapan dan Pengerjaan
| Nama | NIM | Pekerjaan | Jam Persiapan dan Pengerjaan |
| - | - | - | - | 
| Timothy Stanley Setiawan | 13520028 | Implementasi Fitur Pembayaran, Utilisasi Fitur Keranjang, Error Handling  | 50 Jam |
| Adzka Ahmadetya Zaidan | 13520127 |  Implementasi Fitur Twibbon, Splash Screen, Utilisasi UX | 50 Jam |
| Fachry Dennis Heraldi | 13520139 | Implementasi Fitur Daftar Menu, Daftar Cabang, Daftar Keranjang  | 50 Jam |


