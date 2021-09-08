package com.example.pdf_memo;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    Context context;
    Button mCreatePDF;

    final static int conn = 3333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreatePDF = findViewById(R.id.PDf);
        context = this;


        mCreatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkPermission()) {
                    CreatePDFFile(context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");
                    Toast.makeText(context, "PDF Generated", Toast.LENGTH_SHORT).show();
                } else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Allow Storage Permission to Generate and Store PDF").setTitle("Storage Permission Required");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            RequestPermission();
                        }
                    });

                    builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            Toast.makeText(context, "Allow the Storage Permission to Generate PDF", Toast.LENGTH_LONG).show();
                        }
                    });

                    builder.show();

                }

            }
        });



////        Check_Permissions();
//
//        Dexter.withActivity(this).withPermission(WRITE_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//
//                        mCreatePDF.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                try {
//                                    CreatePDFFile(context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");
//                                    Toast.makeText(MainActivity.this, "Button Pressed", Toast.LENGTH_SHORT).show();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//
////                                    Log.e("Awais ", e.getLocalizedMessage());
//                                    Toast.makeText(context, " Error : " + e.getLocalizedMessage(),  Toast.LENGTH_LONG).show();
//                                    mtv.setText(e.getLocalizedMessage());
//                                }
//                            }
//
//                        });
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//
//                    }
//                }).check();


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case conn:
                if (grantResults.length > 0){

                    boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (storage && read) {

                        // Go to your activity or do your other work

                        mCreatePDF.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CreatePDFFile(context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");
                                Toast.makeText(context, "PDF Generated", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        // Show that permissions are not granted

//                        Toast.makeText(context, "Allow Storage permission to generate the pdf", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

                if (Environment.isExternalStorageManager()) {

                    // do your Work here

                    CreatePDFFile(context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");
                    Toast.makeText(context, "PDF Generated", Toast.LENGTH_SHORT).show();

                } else {

//                    Toast.makeText(context, "PDF Not Generated because of permission restriction", Toast.LENGTH_LONG).show();
                }
            }

        }
    }



    public void RequestPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[] {getApplicationContext().getPackageName()})));
                startActivityForResult(intent, 2000);
            }catch (Exception e){

                Intent obj = new Intent();
                obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(obj, 2000);

            }

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, conn);

        }

    }


    public boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            return Environment.isExternalStorageManager();

        } else{

            int write = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED
                    && read == PackageManager.PERMISSION_GRANTED;


        }


    }












    private void CreatePDFFile(String path) {

        if (new File(path).exists())
            new File(path).delete();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));

            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Muneer APPS");
//            document.addCreator("MBR");


            addNewItem(document, "Muneer Apps MirpurKhas", Element.ALIGN_CENTER);
            addNewItem(document, "Invoice (Generated)", Element.ALIGN_CENTER);
            addLineSeparator(document);
            addLineSeparator(document);

            addLineSpace(document);
            addNewItem(document, "\n", Element.ALIGN_CENTER);

            addNewItem(document, "Order Details" , Element.ALIGN_CENTER);

            addNewItem(document, "Order Title : Demo ", Element.ALIGN_LEFT); // purchase or sell
            addNewItem(document, "Order Date and Time : 20 January 2021 " , Element.ALIGN_LEFT);

            addLineSeparator(document);
            addNewItem(document, "Customer Details", Element.ALIGN_CENTER);

            addLineSpace(document);
            addNewItem(document, "Customer Name : Ahmad ", Element.ALIGN_LEFT);
            addNewItem(document, "Customer Email : Abcd@gmail.com " , Element.ALIGN_LEFT);

            addLineSpace(document);


            addNewItem(document, "Category : Bakery Item " , Element.ALIGN_LEFT);

            addLineSeparator(document);
            addNewItemWithLeftAndRight(document, "Item Name", "Rate" , "Quantity", "Amount");

            addLineSeparator(document);

            addNewItemWithLeftAndRight(document,"Nimco", "10 Rs." ,"25", "250 Rs.");
            addNewItemWithLeftAndRight(document,"Cake", "10 Rs." ,"15", "150 Rs.");
            addNewItemWithLeftAndRight(document,"Biscuit", "10 Rs." ,"22", "220 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");
            addNewItemWithLeftAndRight(document,"Abcd", "10 Rs." ,"5", "50 Rs.");
            addNewItemWithLeftAndRight(document,"Papay", "10 Rs." ,"25", "250 Rs.");
            addNewItemWithLeftAndRight(document,"Tria", "10 Rs." ,"35", "350 Rs.");
            addNewItemWithLeftAndRight(document,"Exer", "10 Rs." ,"40", "440 Rs.");
            addNewItemWithLeftAndRight(document,"Tira", "10 Rs." ,"32", "320 Rs.");
            addNewItemWithLeftAndRight(document,"wista", "10 RS." ,"55", "550 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");
            addNewItemWithLeftAndRight(document,"Abcd", "10 Rs." ,"5", "50 Rs.");
            addNewItemWithLeftAndRight(document,"Papay", "10 Rs." ,"25", "250 Rs.");
            addNewItemWithLeftAndRight(document,"Cake", "10 Rs." ,"15", "150 Rs.");
            addNewItemWithLeftAndRight(document,"Biscuit", "10 Rs." ,"22", "220 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");
            addNewItemWithLeftAndRight(document,"Cake", "10 Rs." ,"15", "150 Rs.");
            addNewItemWithLeftAndRight(document,"Biscuit", "10 Rs." ,"22", "220 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");

            addNewItemWithLeftAndRight(document,"Cake", "10 Rs." ,"15", "150 Rs.");
            addNewItemWithLeftAndRight(document,"Biscuit", "10 Rs." ,"22", "220 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");
            addNewItemWithLeftAndRight(document,"Abcd", "10 Rs." ,"5", "50 Rs.");
            addNewItemWithLeftAndRight(document,"Papay", "10 Rs." ,"25", "250 Rs.");
            addNewItemWithLeftAndRight(document,"Tria", "10 Rs." ,"35", "350 Rs.");
            addNewItemWithLeftAndRight(document,"Exer", "10 Rs." ,"40", "440 Rs.");
            addNewItemWithLeftAndRight(document,"Tira", "10 Rs." ,"32", "320 Rs.");
            addNewItemWithLeftAndRight(document,"wista", "10 RS." ,"55", "550 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");
            addNewItemWithLeftAndRight(document,"Abcd", "10 Rs." ,"5", "50 Rs.");
            addNewItemWithLeftAndRight(document,"Papay", "10 Rs." ,"25", "250 Rs.");
            addNewItemWithLeftAndRight(document,"Cake", "10 Rs." ,"15", "150 Rs.");
            addNewItemWithLeftAndRight(document,"Biscuit", "10 Rs." ,"22", "220 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");
            addNewItemWithLeftAndRight(document,"Cake", "10 Rs." ,"15", "150 Rs.");
            addNewItemWithLeftAndRight(document,"Biscuit", "10 Rs." ,"22", "220 Rs.");
            addNewItemWithLeftAndRight(document,"Rusk", "10 Rs." ,"12", "120 Rs.");
            addNewItemWithLeftAndRight(document,"Sweet", "10 Rs." ,"20", "200 Rs.");
            addNewItemWithLeftAndRight(document,"Abcd", "10 Rs." ,"5", "50 Rs.");
            addNewItemWithLeftAndRight(document,"Papay", "10 Rs." ,"25", "250 Rs.");
            addNewItemWithLeftAndRight(document,"Tria", "10 Rs." ,"35", "350 Rs.");


            // Total
            addLineSpace(document);
            addLineSeparator(document);
            addLineSeparator(document);


            addNewItemWithLeftAndRight(document, " Total ", "" , "" , "1850 Rs." );

            document.close();
//            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

            PrintPDF();

        }


        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


//    public void Check_Permissions()
//    {
//        if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED)
//        {
//
//        }
//        else {
//            requestStoragePermission();
//        }
//    }
//
//    private void requestStoragePermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE))
//        {
//            new AlertDialog.Builder(this)
//                    .setTitle("Permission needed")
//                    .setMessage("This Muneer Apps required your Storage Permission")
//                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE
//                                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            1))
//                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .create().show();
//        }
//        else {
//            ActivityCompat.requestPermissions(this,
//                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE
//                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
//        {
//            Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
//
//        }
//    }
//
//







//            // settings
//            document.setPageSize(PageSize.A4);
//            document.addCreationDate();
//            document.addAuthor("MBR Bilal");

//            addNewItem(document, "Muneer Apps MirpurKhas", Element.ALIGN_CENTER);
//            addNewItem(document, "Invoice (Generated)", Element.ALIGN_CENTER);
//            addLineSeparator(document);
//            addLineSeparator(document);


    private void PrintPDF() {

        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        try
        {
            PrintDocumentAdapter printDocumentAdapter =
                    new PdfDocumentAdapter(context, context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());

        } catch (Exception ex)
        {
            Toast.makeText(context, "error " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



    private void addNewItemWithLeftAndRight(Document document, String ItemName, String ItemRate , String ItemQuantity , String ItemAmount) throws DocumentException {


        PdfPTable table = new PdfPTable(4);

        PdfPCell item = new PdfPCell(new Phrase(ItemName));
        PdfPCell rate = new PdfPCell(new Phrase(ItemRate));
        PdfPCell quantity = new PdfPCell(new Phrase(ItemQuantity));
        PdfPCell amount = new PdfPCell(new Phrase(ItemAmount));

        table.addCell(item);
        table.addCell(rate);
        table.addCell(quantity);
        table.addCell(amount);

        document.add(table);

    }


    private void addLineSeparator(Document document) throws DocumentException {

        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);

    }


    private void addLineSpace(Document document) throws DocumentException {

        document.add(new Paragraph(""));
    }



    private void addNewItem(Document document, String text, int align) throws DocumentException {

        Chunk chunk = new Chunk(text);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }


}


