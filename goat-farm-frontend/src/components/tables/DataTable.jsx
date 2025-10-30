import PropTypes from 'prop-types';

function DataTable({ columns, data, isLoading, emptyMessage = 'No records found.' }) {
  if (isLoading) {
    return <div className="text-center py-5">Loading...</div>;
  }

  if (!data.length) {
    return <div className="text-center py-5 text-muted">{emptyMessage}</div>;
  }

  return (
    <div className="table-responsive">
      <table className="table align-middle">
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.key || col.header}>{col.header}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row) => (
            <tr key={row.id || row.referenceNo}>
              {columns.map((col) => (
                <td key={col.key || col.header}>
                  {col.render ? col.render(row[col.accessor], row) : row[col.accessor]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

DataTable.propTypes = {
  columns: PropTypes.arrayOf(PropTypes.shape({
    header: PropTypes.string.isRequired,
    accessor: PropTypes.string.isRequired,
    render: PropTypes.func,
    key: PropTypes.string
  })).isRequired,
  data: PropTypes.array.isRequired,
  isLoading: PropTypes.bool,
  emptyMessage: PropTypes.string
};

DataTable.defaultProps = {
  isLoading: false,
  emptyMessage: 'No records found.'
};

export default DataTable;
