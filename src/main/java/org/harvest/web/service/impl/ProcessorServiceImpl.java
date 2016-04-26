package org.harvest.web.service.impl;

import java.util.List;

import org.harvest.web.bean.Processor;
import org.harvest.web.dao.IProcessorDao;
import org.harvest.web.service.IProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessorServiceImpl implements IProcessorService {

	@Autowired
	private IProcessorDao mProcessorDao;
	
	@Override
	public List<Processor> queryProcessorList(String proc_name, int page, int rows) {
		return mProcessorDao.queryProcessorList(proc_name, (page - 1) * rows, rows);
	}
	
	@Override
	public Integer queryProcessorCount(String proc_name) {
		return mProcessorDao.queryProcessorCount(proc_name);
	}

	@Override
	public int updateProcessor(Processor processor) {
		return mProcessorDao.updateProcessor(processor);
	}

	@Override
	public int addProcessor(Processor processor) {
		return mProcessorDao.addProcessor(processor);
	}

	@Override
	public int deleteProcessor(List<String>  proc_classes) {
		Integer result = 0;
		for (String proc_class : proc_classes) {
			result += this.mProcessorDao.deleteProcessor(proc_class);
		}
		return result;
	}
	
	@Override
	public List<Processor> queryProcessorOptions() {
		return mProcessorDao.queryProcessorOptions();
	}

}
