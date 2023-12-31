# Compiler


## توضیحات فاز دوم 

در فاز دوم پروژه سعی بر بررسی کلمات، اپراتورها، کلمات کلیدی، آیدی ها و... را داشتیم. هدف این بود تا به برنامه یک قطعه کد به عنوان
ورودی داده شود و برنامه با بررسی جزئیات برنامه دنباله ای از توکن های بدست امده را به خروجی تحویل دهد.
پس به عنوان مدل بندی
ابتدا NFA مسیر رسم و کلمات کلیدی و اپراتورها شناسایی و نقشه راه مشخص شد. در مرحله بعد NFA به DFA جهت سهولت در پیاده
سازی تبدیل گردید و DFA حاصل پیاده سازی شد.


### DFA:

![DFA](https://s4.uupload.ir/files/screenshot_2021-11-29_170022_4q1d.jpg)


## نحوه‌ی اجرای فاز دوم:
ابتدا از طریق لینک زیر فایل Compiler.zip را دانلود کنید
سپس فایل فشرده را استخراج کنید

[Compiler.zip](https://www.dropbox.com/s/sndvi1pcum2gq6z/Compiler.zip?dl=1)

در فایل input.txt کد را قرار دهید و سپس برای اجرا cmd را باز کرده و به مسیری که فایل دانلود شده را استحراج کردید بروید و عبارت زیر را وارد کنید

``` java -jar Compiler.jar ```

فایل output2.txt که حاوی خروجی است در همان پوشه ساخته شد. 

تمام فایل کلاس ها هم در پوشه src موجود است





## توضیحات فاز سوم
در فاز سوم ابتدا گرامر تولید کردیم و سپس با استفاده از جداول first و follow جدول ll1 را رسم کردیم. 
سپس گرامر و جدول را در پروژه پیاده‌سازی کردیم و در نهایت از طریق توکن های به‌دست اماده در فاز دوم و جدول ll1 درخت پارس را تولید کردیم

[GRAMMAR](https://docs.google.com/document/d/1mG9heVPoULbgHrJLcSdEYPJmgtsj5df0ojlpV4I2J1g/edit?usp=sharing)

[LL1_TABLE](https://docs.google.com/spreadsheets/d/1wW4Hoh3cCdoQ_71NzaZRJAiOqrRiMZJtexfvWC-5vhE/edit?usp=sharing)


## نحوه‌ی اجرای فاز سوم:

از طریق لینک زیر می‌توانید نسخه اندروید پروژه را دانلود کرده و آن را روی دستگاه یا شبیه‌سازتان اجرا کنید

[Compiler.apk](https://www.dropbox.com/s/cgknx32b7p1arz5/Compiler.apk?dl=1)

[Compiler.gif](https://github.com/CompilerTeam4/Compiler/blob/master/Compiler.gif)
