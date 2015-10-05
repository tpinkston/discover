#include <dirent.h>
#include <errno.h>
#include <exception>
#include <fstream>
#include <iostream>
#include <map>
#include <sstream>
#include <stdlib.h>
#include <string>
#include <sys/types.h>
#include <vector>

class entry_type {

  public:

    std::string
        name,
        description;
};

typedef __dirstream
    directory;
typedef struct dirent
    entry;
typedef std::map<int, entry_type>
    entries_type;
typedef std::map<std::string, entries_type>
    enumerations_type;

bool parse_directory(std::map<std::string, std::string> &files);
bool parse_file(const std::string &file, const std::string &path);
bool write_source(void);
bool write_source_names(void);
bool write_source_values(void);
bool write_source_descriptions(void);

namespace {

    const std::string
        file_name = "enum_generated",
        source = "src/discover/vdis/enums/VDIS.java",
        source_names = "src/discover/vdis/enums/VDISNames.java",
        source_values = "src/discover/vdis/enums/VDISValues.java",
        source_descriptions = "src/discover/vdis/enums/VDISDescriptions.java",
        resources = "enums/csv";
    std::map<char, std::string>
        swap;
    enumerations_type
        enumerations;
    int
        total = 0;
}

// ---------------------------------------------------------------------------
int main(int argc, char *argv[]) {

    std::map<std::string, std::string>
        files;
    int
        result = 0;

    // Characters the get swapped in enumeration names and descriptions.
    swap['~'] = ",";
    swap['#'] = "P";

    std::cout << "Building enumerations..." << std::endl;

    if (not parse_directory(files)) {

        result = 1;
    }
    else {

        std::map<std::string, std::string>::const_iterator
            itor = files.begin();

        while((result == 0) and (itor != files.end())) {

            if (not parse_file(itor->first, itor->second)) {

                result = 2;
            }

            ++itor;
        }
    }

    if (result == 0) {

        std::cout << total << " total enumerations parsed from "
                  << files.size() << " files..." << std::endl;

        if (write_source() and
            write_source_descriptions() and
            write_source_names() and
            write_source_values()) {

            std::cout << "Enumerations built successfully." << std::endl;
        }
        else {

            result = 3;
        }
    }

    return result;
}

// ---------------------------------------------------------------------------
bool parse_directory(std::map<std::string, std::string> &files) {

    directory
        *directory_ptr = 0;
    entry
        *entry_ptr = 0;
    bool
        success = true;

    directory_ptr = opendir(resources.c_str());

    if (directory_ptr == NULL) {

        std::cerr << "Could not open directory: " << resources << std::endl;
        success = false;
    }
    else {

        while((entry_ptr = readdir(directory_ptr)) != NULL) {

            std::string
                file = std::string(entry_ptr->d_name);

            if ((file != ".") and (file != "..") and (file != ".svn")) {

                files[file] = (resources + "/" + file);
            }
        }

        closedir(directory_ptr);
    }

    return success;
}

// ---------------------------------------------------------------------------
bool parse_file(const std::string &file, const std::string &path) {

    std::string
        temp_file = file;
    bool
        success = true;

    try {

        std::ifstream
            input(path.c_str());
        std::string
            name = temp_file.replace(temp_file.find(".CSV"), 4, "");

        if (not input.good()) {

            std::cerr << "Could not open file: " << path << std::endl;
            success = false;
        }
        else {

            entries_type
                &entries = enumerations[name];
            std::string
                line,
                token;
            bool
                first = true;
            int
                name_index = 1,
                value_index = 0,
                description_index = 2;

            while(std::getline(input, line)) {

                std::stringstream
                    stream(line);
                std::vector<std::string>
                    tokens;
                int
                    index = 0;

                while(std::getline(stream, token, ',')) {

                    tokens.push_back(token);

                    if (first) {

                        if (token == "name") {

                            name_index = index;
                        }
                        else if (token == "value") {

                            value_index = index;
                        }
                        else if (token == "desc") {

                            description_index = index;
                        }
                    }

                    ++index;
                }

                if ((tokens.size() < 3)) {

                }
                else if (not first and not tokens[0].empty()) {

                    int
                        value = atoi(tokens[value_index].c_str());
                    entry_type
                        entry;

                    entry.name = tokens[name_index];
                    entry.description = tokens[description_index];

                    // In the EMITTER_NAME file there is no "desc" field but
                    // three other description field: "national_desc",
                    // "nato_desc", and "comm_designation_desc".  If one does
                    // not apply then it will be "0".
                    if (entry.description == "0") {

                        entry.description = tokens[description_index + 1];
                    }

                    if (entry.description == "0") {

                        entry.description = tokens[description_index + 2];
                    }

                    std::map<char, std::string>::const_iterator
                        swap_itor = swap.begin();

                    while(swap_itor != swap.end()) {

                        while(entry.name.find(swap_itor->first) !=
                              std::string::npos) {

                            entry.name.replace(
                                entry.name.find(swap_itor->first),
                                1,
                                swap_itor->second);
                        }

                        while(entry.description.find(swap_itor->first) !=
                              std::string::npos) {

                            entry.description.replace(
                                entry.description.find(swap_itor->first),
                                1,
                                swap_itor->second);
                        }

                        ++swap_itor;
                    }

                    entries[value] = entry;
                }

                if (first) {

                    first = false;
                }
            }

            input.close();

            total += entries.size();
        }
    }
    catch(std::exception &exception) {

        std::cerr << "Exception with '" << exception.what()
                  << "' parsing file '" << file << "'" << std::endl;
    }

    return success;
}

// ---------------------------------------------------------------------------
void write_handle_class(std::ofstream &output) {

    output << "    public static class Handle {" << std::endl
           << std::endl
           << "        public final String name;" << std::endl
           << "        public final int[] values;" << std::endl
           << "        public final String[] names;" << std::endl
           << "        public final String[] descriptions;" << std::endl
           << std::endl
           << "        public Handle(" << std::endl
           << "            String name," << std::endl
           << "            int values[]," << std::endl
           << "            String names[]," << std::endl
           << "            String descriptions[]) {" << std::endl
           << std::endl
           << "            this.name = name;" << std::endl
           << "            this.values = values;" << std::endl
           << "            this.names = names;" << std::endl
           << "            this.descriptions = descriptions;" << std::endl
           << "        }" << std::endl
           << std::endl
           << "        public String getName(int value) {" << std::endl
           << std::endl
           << "            for(int i = 0, size = values.length; i < size; ++i) {" << std::endl
           << std::endl
           << "                if (value == values[i]) {" << std::endl
           << std::endl
           << "                    return names[i];" << std::endl
           << "                }" << std::endl
           << "            }" << std::endl
           << std::endl
           << "            return getUnknown(name, value);" << std::endl
           << "        }" << std::endl
           << std::endl
           << "        public String getDescription(int value) {" << std::endl
           << std::endl
           << "            for(int i = 0, size = values.length; i < size; ++i) {" << std::endl
           << std::endl
           << "                if (value == values[i]) {" << std::endl
           << std::endl
           << "                    return descriptions[i];" << std::endl
           << "                }" << std::endl
           << "            }" << std::endl
           << std::endl
           << "            return getUnknown(name, value);" << std::endl
           << "        }" << std::endl
           << "    }" << std::endl
           << std::endl;
}

// ---------------------------------------------------------------------------
void write_static_block(std::ofstream &output) {

    output << "    private static final HashMap<Integer, Handle> handles;" << std::endl
           << "    private static final HashMap<String, HashMap<Integer, String>> unknowns;" << std::endl
           << std::endl
           << "    static {" << std::endl
           << std::endl
           << "        handles = new HashMap<Integer, Handle>();" << std::endl
           << "        unknowns = new HashMap<String, HashMap<Integer, String>>();" << std::endl
           << "    }" << std::endl
           << std::endl;
}

// ---------------------------------------------------------------------------
void write_pdu_types(std::ofstream &output) {

    enumerations_type::const_iterator
        itor = enumerations.find("PDU_TYPE");
    int i, count;

    if (itor != enumerations.end()) {

        const entries_type
            &entries = itor->second;
        entries_type::const_iterator
            entry_itor = entries.begin();
        int
            i = 0,
            count = (entries.size() - 1);

        output << "    public static final int" << std::endl;

        while(entry_itor != entries.end()) {

            output << "        " << entry_itor->second.name
                   << " = " << entry_itor->first
                   << ((i < count) ? "," : ";") << std::endl;

            ++i;
            ++entry_itor;
        }

        output << std::endl;
    }
}

// ---------------------------------------------------------------------------
void write_enum_types(std::ofstream &output) {

    enumerations_type::const_iterator
        itor = enumerations.begin();
    int
        i = 0,
        count = (enumerations.size() - 1);

    output << "    public static final int" << std::endl;

    while(itor != enumerations.end()) {

        output << "        " << itor->first << " = " << i
               << ((i < count) ? "," : ";") << std::endl;

        ++i;
        ++itor;
    }

    output << std::endl;
}

// ---------------------------------------------------------------------------
void write_enum_type_names(std::ofstream &output) {

    enumerations_type::const_iterator
        itor = enumerations.begin();
    int
        i = 0,
        count = (enumerations.size() - 1);

    output << "    public static final String ENUM_TYPE_NAMES[] = {"
           << std::endl;

    while(itor != enumerations.end()) {

        output << "        \"" << itor->first << "\""
               << ((i < count) ? "," : " ") << std::endl;

        ++i;
        ++itor;
    }

    output << "    };" << std::endl << std::endl;
}

// ---------------------------------------------------------------------------
void write_enum_values(std::ofstream &output) {

    enumerations_type::const_iterator
        itor = enumerations.begin();
    entries_type::const_iterator
        entry_itor;
    int
        i = 0,
        count = (enumerations.size() - 1);

    while(itor != enumerations.end()) {

        const entries_type
            &entries = itor->second;
        const int
            value_count = entries.size();

        output << "    public static final int "
               << itor->first << "[] = { " << std::endl;

        for(entry_itor = entries.begin(), i = 0; (i < value_count); ++i) {

            output << "        " << entry_itor->first
                   << ((i < (value_count - 1)) ? ", " : "  ")
                   << "// " << entry_itor->second.name
                   << std::endl;

            ++entry_itor;
        }

        output << "    };" << std::endl << std::endl;

        ++itor;
    }
}

// ---------------------------------------------------------------------------
void write_enum_names(std::ofstream &output) {

    enumerations_type::const_iterator
        itor = enumerations.begin();
    entries_type::const_iterator
        entry_itor;
    int
        i = 0,
        count = (enumerations.size() - 1);

    while(itor != enumerations.end()) {

        const entries_type
            &entries = itor->second;
        const int
            value_count = entries.size();

        output << "    public static final String "
               << itor->first << "[] = { " << std::endl;

        for(entry_itor = entries.begin(), i = 0; (i < value_count); ++i) {

            output << "        \"" << entry_itor->second.name << "\""
                   << ((i < (value_count - 1)) ? ", " : "  ")
                   << "// " << entry_itor->first
                   << std::endl;

            ++entry_itor;
        }

        output << "    };" << std::endl << std::endl;

        ++itor;
    }
}

// ---------------------------------------------------------------------------
void write_enum_description(std::ofstream &output) {


    enumerations_type::const_iterator
        itor = enumerations.begin();
    entries_type::const_iterator
        entry_itor;
    int
        i = 0,
        count = (enumerations.size() - 1);

    while(itor != enumerations.end()) {

        const entries_type
            &entries = itor->second;
        const int
            value_count = entries.size();

        output << "    public static final String "
               << itor->first << "[] = { " << std::endl;

        for(entry_itor = entries.begin(), i = 0; (i < value_count); ++i) {

            output << "        \"" << entry_itor->second.description << "\""
                   << ((i < (value_count - 1)) ? ", " : "  ")
                   << "// " << entry_itor->first << " = "
                   << entry_itor->second.name << std::endl;

            ++entry_itor;
        }

        output << "    };" << std::endl << std::endl;

        ++itor;
    }
}

// ---------------------------------------------------------------------------
void write_methods(std::ofstream &output) {

    enumerations_type::const_iterator
        enum_itor;

    // ***********************************************************************
    // getEnumValues
    // ***********************************************************************

    output << "    public static int[] getEnumValues(int type) {" << std::endl
           << std::endl
           << "        switch(type) {" << std::endl << std::endl;

    enum_itor = enumerations.begin();

    while(enum_itor != enumerations.end()) {

        output << "            case " + enum_itor->first
               << ": return VDISValues." << enum_itor->first << ";"
               << std::endl;

        ++enum_itor;
    }

    output << "            default: "
           << "System.err.println(\"No values for type \" + type);"
           << std::endl
           << "        }" << std::endl
           << std::endl
           << "        return null;" << std::endl
           << "    }" << std::endl
           << std::endl;

    // ***********************************************************************
    // getEnumNames
    // ***********************************************************************

    output << "    public static String[] getEnumNames(int type) {"
           << std::endl << std::endl
           << "        switch(type) {" << std::endl << std::endl;

    enum_itor = enumerations.begin();

    while(enum_itor != enumerations.end()) {

        output << "            case " + enum_itor->first
               << ": return VDISNames." << enum_itor->first << ";"
               << std::endl;

        ++enum_itor;
    }

    output << "            default: "
           << "System.err.println(\"No names for type \" + type);" << std::endl
           << "        }" << std::endl << std::endl
           << "        return null;" << std::endl
           << "    }" << std::endl
           << std::endl;

    // ***********************************************************************
    // getEnumDescriptions
    // ***********************************************************************

    output << "    public static String[] getEnumDescriptions(int type) {"
           << std::endl << std::endl
           << "        switch(type) {" << std::endl << std::endl;

    enum_itor = enumerations.begin();

    while(enum_itor != enumerations.end()) {

        output << "            case " + enum_itor->first
               << ": return VDISDescriptions." << enum_itor->first << ";"
               << std::endl;

        ++enum_itor;
    }

    output << "            default: "
           << "System.err.println(\"No descriptions for type \" + type);"
           << std::endl
           << "        }" << std::endl << std::endl
           << "        return null;" << std::endl
           << "    }" << std::endl
           << std::endl;

    // ***********************************************************************
    // getTypeName
    // ***********************************************************************

    output << "    public static String getTypeName(int type) {"
           << std::endl << std::endl
           << "        if ((type >= 0) && (type < "
           << enumerations.size() << ")) {" << std::endl << std::endl
           << "            return ENUM_TYPE_NAMES[type];" << std::endl
           << "        }" << std::endl << std::endl
           << "        System.err.println(\"Invalid type: \" + type);"
           << std::endl << std::endl
           << "        return null;" << std::endl
           << "    }" << std::endl
           << std::endl;


    // ***********************************************************************
    // getName
    // ***********************************************************************

    output << "    public static String getName(int type, int value) {" << std::endl
           << std::endl
           << "       return getHandle(type).getName(value);" << std::endl
           << "    }" << std::endl
           << std::endl;

    // ***********************************************************************
    // getDescription
    // ***********************************************************************

    output << "    public static String getDescription(int type, int value) {" << std::endl
           << std::endl
           << "        return getHandle(type).getDescription(value);" << std::endl
           << "    }" << std::endl
           << std::endl;

    // ***********************************************************************
    // getUnknown
    // ***********************************************************************

    output << "    public static String getUnknown(String name, int value) {" << std::endl
           << std::endl
           << "        HashMap<Integer, String> typeUnknowns = unknowns.get(name);" << std::endl
           << std::endl
           << "        if (typeUnknowns == null) {" << std::endl
           << std::endl
           << "            typeUnknowns = new HashMap<Integer, String>();" << std::endl
           << std::endl
           << "            unknowns.put(name, typeUnknowns);" << std::endl
           << "        }" << std::endl
           << std::endl
           << "       String unknown = typeUnknowns.get(value);" << std::endl
           << std::endl
           << "        if (unknown == null) {" << std::endl
           << std::endl
           << "            unknown = (name + \"_\" + value);" << std::endl
           << std::endl
           << "            typeUnknowns.put(value, unknown);" << std::endl
           << "        }" << std::endl
           << std::endl
           << "        return unknown;" << std::endl
           << "}" << std::endl
           << std::endl;

    // ***********************************************************************
    // getHandle
    // ***********************************************************************

    output << "    public static Handle getHandle(int type) {" << std::endl
           << std::endl
           << "         Handle handle = handles.get(type);" << std::endl
           << std::endl
           << "         if (handle == null) {" << std::endl
           << std::endl
           << "             handle = new Handle(" << std::endl
           << "                  getTypeName(type)," << std::endl
           << "                  getEnumValues(type)," << std::endl
           << "                  getEnumNames(type)," << std::endl
           << "                  getEnumDescriptions(type));" << std::endl
           << std::endl
           << "             handles.put(type, handle);" << std::endl
           << "         }" << std::endl
           << std::endl
           << "         return handle;" << std::endl
           << "     }" << std::endl
           << std::endl;
}

// ---------------------------------------------------------------------------
bool write_source(void) {

    std::ofstream
        output(source.c_str());
    bool
        success = true;

    if (not output.good()) {

        std::cerr << "Could not open file: " << source << std::endl;
        success = false;
    }
    else {

        std::cout << "Writing source file '" << source << "'" << std::endl;

        output << "package discover.vdis.enums;" << std::endl << std::endl
               << "import java.util.HashMap;" << std::endl << std::endl
               << "/**"  << std::endl
               << " * Auto-generated class containing VDIS enumeration data."
               << std::endl
               << " */"  << std::endl
               << "public class VDIS {" << std::endl << std::endl;

        write_handle_class(output);
        write_static_block(output);
        write_pdu_types(output);
        write_enum_types(output);
        write_enum_type_names(output);
        write_methods(output);

        output << "}" << std::endl;

        output.close();
    }

    return success;
}

// ---------------------------------------------------------------------------
bool write_source_names(void) {

    std::ofstream
        output(source_names.c_str());
    bool
        success = true;

    if (not output.good()) {

        std::cerr << "Could not open file: " << source_names << std::endl;
        success = false;
    }
    else {


        std::cout << "Writing source file '"
                  << source_names << "'" << std::endl;

        output << "package discover.vdis.enums;" << std::endl << std::endl
               << "/**"  << std::endl
               << " * Auto-generated class containing VDIS enumeration data."
               << std::endl
               << " */"  << std::endl
               << "public class VDISNames {" << std::endl << std::endl;

        write_enum_names(output);

        output << "}" << std::endl;

        output.close();
    }

    return success;
}

// ---------------------------------------------------------------------------
bool write_source_values(void) {

    std::ofstream
        output(source_values.c_str());
    bool
        success = true;

    if (not output.good()) {

        std::cerr << "Could not open file: " << source_values << std::endl;
        success = false;
    }
    else {


        std::cout << "Writing source file '"
                  << source_values << "'" << std::endl;

        output << "package discover.vdis.enums;" << std::endl << std::endl
               << "/**"  << std::endl
               << " * Auto-generated class containing VDIS enumeration data."
               << std::endl
               << " */"  << std::endl
               << "public class VDISValues {" << std::endl << std::endl;

        write_enum_values(output);

        output << "}" << std::endl;

        output.close();
    }

    return success;
}

// ---------------------------------------------------------------------------
bool write_source_descriptions(void) {

    std::ofstream
        output(source_descriptions.c_str());
    bool
        success = true;

    if (not output.good()) {

        std::cerr << "Could not open file: "
                  << source_descriptions << std::endl;
        success = false;
    }
    else {


        std::cout << "Writing source file '"
                  << source_descriptions << "'" << std::endl;

        output << "package discover.vdis.enums;" << std::endl << std::endl
               << "/**"  << std::endl
               << " * Auto-generated class containing VDIS enumeration data."
               << std::endl
               << " */"  << std::endl
               << "public class VDISDescriptions {" << std::endl << std::endl;

        write_enum_description(output);

        output << "}" << std::endl;

        output.close();
    }

    return success;
}
