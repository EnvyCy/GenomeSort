//Jan Cichy - 1
import java.util.Scanner;
//Działanie programu polega na sortowaniu kodonów z wejściowego genomu.
//Do wykonania zadania korzystam z dodatkowej tablicy, w której przechowuję
//kody liczbowe odpowiednich aminokwasów każdego kodonu i to na niej wykonuję algorytm Quicksort.
//Po posortowaniu zamieniam kody na odpowiadające im znaki ASCII i je wypisuję na wyjście.
//Rozwiązanie spełnia wymagania zadania, gdyż korzystam z QuickSort w wersji bez rekurencji i bez stosu.
//Korzystam również z mniejszej ilości tablic niż maksymalna oraz algorytm nie wykona się jeżeli
//wejściowy genom nie spełnia odpowiednich wymagań.
class Codones{
    public String[] codones;     //tablica kodonów
    public int[] codearray;     //tablica z kodami kodonu
    public int startindex = 0;
    public int stopindex = 0;
    public int length;

    public void Group(String input){                //funkcja grupująca aminokwasy w kodony
        length = ((stopindex - startindex) / 3) - 1;        //ilość kodonów
        codones = new String[length];
        codearray = new int[length];

        for(int i = 0; i < length; i++)
            codones[i] = "";

        for(int i = startindex + 3, j = 0; j < length; i += 3, j++){
            codones[j] += input.charAt(i);                      //pierwszy aminokwas kodonu
            codones[j] += input.charAt(i + 1);                  //drugi
            codones[j] += input.charAt(i + 2);                  //trzeci
            codearray[j] += (int)input.charAt(i) * 10000;       //kod pierwszego aminokwasu
            codearray[j] += (int)input.charAt(i + 1) * 100;     //drugiego
            codearray[j] += input.charAt(i + 2);                //trzeciego
        }
    }

    public void Display(){      //funkcja wyświetlająca kodony
        String output = "";
        for(int i = 0; i < length; i++){
            output += codones[i];
        }
        System.out.println(output);
    }

    public int Error(String input){     //funkcja sprawdzająca czy występują errory w wejściowym genomie
        boolean foundstart = false;     //flaga czy znaleźliśmy start
        boolean foundstop = false;      //i stop
        for(int i = 0; i < input.length(); i++)             //jeśli występują inne litery niż poniższe
            if(input.charAt(i) != 'A' && input.charAt(i) != 'C' && input.charAt(i) != 'G' && input.charAt(i) != 'T')
                return 1;                   //"Wrong character in DNA sequence."

        for(int i = 0; i + 2 < input.length() ; i++){               //szukaj kodonu START
            if(input.charAt(i) == 'A' && input.charAt(i + 1) == 'T' && input.charAt(i + 2) == 'G'){
                startindex = i;                                          //ustawiamy indeks startu na obecny
                foundstart = true;                                       //ustawiamy flagę
                for(int j = i; j + 3 <= input.length(); j += 3){     //szukaj kodonu STOP co 3
                    if((input.charAt(j) == 'T' && input.charAt(j + 1) == 'A' && input.charAt(j + 2) == 'A') || (input.charAt(j) == 'T' && input.charAt(j + 1) == 'G' && input.charAt(j + 2) == 'A') || (input.charAt(j) == 'T' && input.charAt(j + 1) == 'A' && input.charAt(j + 2) == 'G')) {
                        stopindex = j;          //ustawiamy indeks stopu na obecny
                        foundstop = true;       //ustawiamy flagę
                    }
                }
            }
            if(foundstop)   //jeśli znaleźliśmy kodon STOP
                break;
        }
        if(!foundstart || !foundstop)       //jeśli nie znaleźliśmy startu lub stopu
            return 2;   //Wrong DNA sequence

        if((stopindex - startindex) == 3)   //stop tuż po starcie
            return 2;       //Wrong DNA sequence

        for(int i = startindex + 3; i <= stopindex - 3; i += 3){        //szukamy co 3 stopu lub startu
            if(input.charAt(i) == 'A' && input.charAt(i + 1) == 'T' && input.charAt(i + 2) == 'G' || (input.charAt(i) == 'T' && input.charAt(i + 1) == 'A' && input.charAt(i + 2) == 'A') || (input.charAt(i) == 'T' && input.charAt(i + 1) == 'G' && input.charAt(i + 2) == 'A') || (input.charAt(i) == 'T' && input.charAt(i + 1) == 'A' && input.charAt(i + 2) == 'G')){
                return 3;   //more than one start/stop codon
            }
        }


        return 0;
    }

    public void QuickSort(int length, int[] arr) {
        int left = 0;
        int right = length - 1;
        int x;
        int i = 0;
        int temp = right;   //element podziału - "pivot"
        while (true) {
            i--;

            while (left < temp) {
                x = Partition(left, temp, arr);     //znajdź element podziału (o 1 w prawo od pivota)
                temp = x - 1;                       //indeks pivota
                ++i;
            }
            if (i < 0)
                break;
            left++;                                 //inkrementujemy lewy indeks
            temp = FindNext(left, length, arr);     //ustawiamy indeks pivota na prawy indeks tablicy
        }
    }

    private int FindNext(int left, int length, int[]arr){       //funkcja zwracająca indeks pivota
        for (int i = left; i < length; ++i)
            if (arr[i] < 0)             //niepotrzebne, zostało po poprzedniej wersji kodu
                return i;

        return length - 1;
    }

    private int Partition(int left, int right, int[] arr) {     //funkcja dokonująca podziału
        long pivot = arr[(left + right) / 2];               //element dzielący

        while (left <= right) {
            while (arr[right] > pivot) right--;     //dopóki element po prawej jest większy niż pivot dawaj indeks w lewo
            while (arr[left] < pivot) left++;       //dopóki element po lewej jest mniejszy niż pivot dawaj indeks w prawo
            if (left <= right) {
                int tmp = arr[right];       //zamień element arr[right] i tmp
                arr[right] = arr[left];
                arr[left] = tmp;

                left++;
                right--;
            }
        }
        return left;       //zwracamy indeks na prawo od pivota
    }

    public void Decode(int[] codearray){    //dekodujemy kod z liczby całkowitej do znaku ASCII
        int code1, code2, code3;
        char char1 = 'O';
        char char2 = 'O';
        char char3 = 'O';
        codones = new String[length];

        for(int i = 0; i < length; i++)
            codones[i] = "";

        for(int i = 0; i < codearray.length; i++){
            code1 = codearray[i]/10000;         //kod pierwszego aminokwasu
            code2 = (codearray[i]%10000)/100;   //drugiego
            code3 = codearray[i]%100;           //trzeciego

            switch(code1){                      //dekodujemy pierwszy aminokwas
                case 65:{
                    char1 = 'A';
                    break;
                }
                case 67:{
                    char1 = 'C';
                    break;
                }
                case 71:{
                    char1 = 'G';
                    break;
                }
                case 84:{
                    char1 = 'T';
                    break;
                }
            }
            switch(code2){              //drugi
                case 65:{
                    char2 = 'A';
                    break;
                }
                case 67:{
                    char2 = 'C';
                    break;
                }
                case 71:{
                    char2 = 'G';
                    break;
                }
                case 84:{
                    char2 = 'T';
                    break;
                }
            }
            switch(code3){          //trzeci
                case 65:{
                    char3 = 'A';
                    break;
                }
                case 67:{
                    char3 = 'C';
                    break;
                }
                case 71:{
                    char3 = 'G';
                    break;
                }
                case 84:{
                    char3 = 'T';
                    break;
                }
            }

            codones[i] += char1;        //wstawiamy znak na miejsce pierwszego aminokwasu
            codones[i] += char2;        //drugiego
            codones[i] += char3;        //trzeciego
        }
    }
}

class Source{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Codones obj = new Codones();
        int amount = scanner.nextInt();
        while(amount != 0) {
            String input = scanner.next();
            input = input.toUpperCase();    //zamień wszystkie litery na duże

            int i = obj.Error(input);
            switch(i){                  //sprawdź czy wejście zawiera error
                case 1:{
                    System.out.println("Wrong character in DNA sequence.");
                    break;
                }
                case 2:{
                    System.out.println("Wrong DNA sequence.");
                    break;
                }
                case 3:{
                    System.out.println("More than one START/STOP codon.");
                    break;
                }
                case 0: {               //jeśli nie zawiera erroru
                    obj.Group(input);
                    obj.QuickSort(obj.codearray.length, obj.codearray);
                    obj.Decode(obj.codearray);
                    obj.Display();
                    break;
                }
            }
            amount--;
        }
    }
}

