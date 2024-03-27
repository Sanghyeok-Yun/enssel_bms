package com.enssel.bms.bi.service;

import com.enssel.bms.bi.entity.CommonCode;
import com.enssel.bms.bi.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;

    public List<CommonCode> getListAll(){
        return commonCodeRepository.findAll();
    }

    public CommonCode getOneByCode(String code) throws Exception {
        return commonCodeRepository.findById(code).orElseThrow(() -> new BadRequestException("해당 code가 존재하지 않습니다."));
    }

    public List<CommonCode> getListByPareCode(String pareCode){
        return commonCodeRepository.findAllByPareCode(pareCode);
    }

    public CommonCode postOne(CommonCode commonCode) throws BadRequestException {
        String pareCode = commonCode.getPareCode();
        String code = commonCode.getCode();
        int sort = commonCode.getSort();

        // 이미 존재하는 code인지 판별
        if(commonCodeRepository.existsById(code)){
            throw new BadRequestException("이미 존재하는 code입니다.");
        }
        // 최상위 코드가 형식에 맞는지 판별
        else if(pareCode.equals("-1")){
            if(code.length()!=3){
                throw new BadRequestException("code가 최상위 code로 적합하지 않습니다.\n최상위 code는 3자리 숫자로 구성되어야 합니다.");
            }
        }
        // pareCode와 code가 정상적인지 판별
        else if(!code.substring(0, code.length()-2).equals(pareCode)){
            throw new BadRequestException("code 혹은 pareCode가 적합하지 않습니다.(code : pareCode + (숫자 2자리)'");
        }

        // 존재하는 pareCode인지 판별
        if(!commonCodeRepository.existsById(pareCode)){
            throw new BadRequestException("pareCode가 존재하지 않습니다.");
        }

        // pareCode의 첫번째 자식인 경우 sort를 1로 처리
        if(!commonCodeRepository.existsByPareCode(pareCode)){
            commonCode.setSort(1);
        }
        // sort가 존재하지 않는 경우의 처리
        else if(sort == 0){
            commonCode.setSort(commonCodeRepository.findFirstByPareCodeOrderBySortDesc(pareCode).getSort()+1);
        }
        // sort가 겹치는 경우, 해당 sort보다 큰 값들의 sort를 1 증가시켜 update
        else if(commonCodeRepository.existsByPareCodeAndSort(pareCode, sort)){
            List<CommonCode> codes = commonCodeRepository.findByPareCodeAndSortGreaterThanEqual(pareCode, sort);
            codes.forEach(cmmCode -> cmmCode.setSort(cmmCode.getSort()+1));
            codes.add(commonCode);
            commonCodeRepository.saveAll(codes);
            return commonCode;
        }

        return commonCodeRepository.save(commonCode);
    }

//    private List<CommonCode> getPutListWithSortIncrease(List<CommonCode> originList, List<CommonCode> updateList){
//        int sort = target.getSort();
//        list.stream()
//                .filter(commonCode -> commonCode.getSort() >= sort)
//                .forEach(cmmCode -> cmmCode.setSort(cmmCode.getSort()+1));
//        list.add(target);
//
//        return list;
//    }
}
