package com.example.majikatubes1.utils

class NumSperator(_num : Int) {
    private var num = _num

    fun parse() : String {
        var str     = ""
        while (num > 0) {
            if (num < 1000) {
                str = "${num}" + str
            } else if (num%1000 > 0) {
                str =  "." + "${num%1000}".padStart(3,'0') + str
            } else {
                str += ".000"
            }
            num /= 1000
        }
        str = "Rp$str"
        return str
    }
}