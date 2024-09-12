package de.expandai.service;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
//import de.expandai.domain.Fingertaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvUploadService {
    //    @Autowired
    //    private FingertapsRepository fingertapsRepository;
    //
    //    public void processFingertapsCsv(MultipartFile file) throws IOException {
    //        List<Fingertaps> fingertaps = new CsvToBeanBuilder<Fingertaps>(new InputStreamReader(file.getInputStream()))
    //            .withType(Fingertaps.class)
    //            .build()
    //            .parse();
    //        fingertapsRepository.saveAll(fingertaps);
    //    }

    // Similar methods for other CSV types
}
