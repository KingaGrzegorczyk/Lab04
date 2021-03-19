package pollub.ism.lab04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Gra extends AppCompatActivity {

    int klikniecia = 0;
    char [][] znak = new char[3][3];
    private String[] zapisanyStatus = new String[9];
    private boolean[] zapisanyWidok = {true,true,true,true,true,true,true,true,true};

    public void kliknijPrzycisk(View view){
        String [] idSplit = (view.getResources().getResourceEntryName(view.getId())).split("_");
        int wiersz = Integer.parseInt(idSplit[1]);
        int kolumna = Integer.parseInt(idSplit[2]);

        klikniecia++;
        if(klikniecia % 2 == 0){
            ((Button) view).setText("X");
            znak[wiersz-1][kolumna-1] = 'X';
            zapisanyStatus[(wiersz-1) * 3 + (kolumna-1)] = "X";
        }
        else{
            ((Button) view).setText("O");
            znak[wiersz-1][kolumna-1] = 'O';
            zapisanyStatus[(wiersz-1) * 3 + (kolumna-1)] = "O";
        }

        view.setEnabled(false);
        if(sprawdzKolumny(znak, 'X') == true || sprawdzWiersze(znak, 'X') == true || sprawdzSkosPierwszy(znak, 'X') == true || sprawdzSkosDrugi(znak, 'X') == true){
            Toast.makeText(this,"Wygrały X", Toast.LENGTH_LONG).show();
            wygrajIWybierzCoDalej("Gratulacje", view);
        }
        else if(sprawdzKolumny(znak, 'O') == true || sprawdzWiersze(znak, 'O') == true || sprawdzSkosPierwszy(znak, 'O') == true || sprawdzSkosDrugi(znak, 'O') == true){
            Toast.makeText(this,"Wygrały O", Toast.LENGTH_LONG).show();
            wygrajIWybierzCoDalej("Gratulacje", view);
        }
        else if(klikniecia == 9){
            Toast.makeText(this,"Remis", Toast.LENGTH_LONG).show();
            wygrajIWybierzCoDalej("Remis", view);
        }

        zapisanyWidok[(wiersz-1) * 3 + (kolumna-1)] = false;
    }

    public void wygrajIWybierzCoDalej(String tytul, View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(tytul + "!");
        alert.setMessage("Chcesz zagrać jeszcze raz?");
        alert.setPositiveButton("Tak", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                grajOdNowa(view);
            }
        });
        alert.setNegativeButton("Nie", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                wrocDoGlownejStrony();
            }
        });
        alert.show();
    }

    public static boolean sprawdzKolumny(char[][] plansza, char symbol){
        for(int kolumna = 0; kolumna < 3; kolumna++){
            boolean wygrana = true;
            for(int wiersz = 0; wiersz < 3; wiersz++) {
                if (plansza[wiersz][kolumna] != symbol) {
                    wygrana = false;
                    break;
                }
            }
            if (wygrana)
                return true;
        }
        return false;
    }

    public static boolean sprawdzWiersze(char[][] plansza, char symbol){
        for(int wiersz = 0; wiersz < 3; wiersz++){
            boolean wygrana = true;
            for(int kolumna = 0; kolumna < 3; kolumna++){
                if (plansza[wiersz][kolumna] != symbol){
                    wygrana = false;
                    break;
                }
            }
            if (wygrana)
                return true;
        }
        return false;
    }

    public static boolean sprawdzSkosPierwszy(char[][] plansza, char symbol){
        for(int i = 0; i < 3; i++){
            if (plansza[i][i] != symbol) {
                return false;
            }
        }
        return true;
    }

    public static boolean sprawdzSkosDrugi(char[][] plansza, char symbol){
        for(int i = 0; i < 3; i++){
            if (plansza[i][3-i-1] != symbol){
                return false;
            }
        }
        return true;
    }

    public void grajOdNowa(View view){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                znak[i][j] = ' ';
            }
        }
        Intent intencja = new Intent(this, Gra.class);
        startActivity(intencja);
    }

    public void wrocDoGlownejStrony(){
        Intent intencja = new Intent(this, MainActivity.class);
        startActivity(intencja);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gra);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button button;
        for(int i = 1; i < 4; i++){
            for(int j = 1; j < 4; j++){
                button = (Button) findViewById(getResources().getIdentifier("b_" + i + "_"+j, "id", this.getPackageName()));
                button.setText(zapisanyStatus[(i-1)*3+(j-1)]);
                button.setEnabled(zapisanyWidok[(i-1)*3+(j-1)]);
                if(zapisanyStatus[(i-1)*3+(j-1)] != null){
                    znak[i-1][j-1] = zapisanyStatus[(i-1)*3+(j-1)].charAt(0);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArray("arrayState",zapisanyStatus);
        outState.putBooleanArray("arrayEnabled",zapisanyWidok);
        outState.putInt("click", klikniecia);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        zapisanyStatus = savedInstanceState.getStringArray("arrayState");
        zapisanyWidok =savedInstanceState.getBooleanArray("arrayEnabled");
        klikniecia = savedInstanceState.getInt("click");
    }
}