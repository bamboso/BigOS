package filesystem;

public class File {

    public File() {

    }

    public File(String name, int block_index, int size) {
        this.name = name;
        this.block_index = block_index;
        this.size = size;
    }

    //unikalna nazwa identyfikujaca plik na dysku twardym
    public String name;

    //numer bloku w którem znajduje siê "blok inteksowy"
    public int block_index;

    //"rozmiar" pliku na dysku twardym
    public int size;

}