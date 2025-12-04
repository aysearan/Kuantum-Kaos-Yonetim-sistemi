using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KuantumKaosYönetimi
{
    
    
        public class KuantumCokusuException : Exception
        {
            public KuantumCokusuException(string id): base($"Kuantum çöküşü gerçekleşti! Patlayan nesne ID: {id}")
            {
            }
        }
    
}
